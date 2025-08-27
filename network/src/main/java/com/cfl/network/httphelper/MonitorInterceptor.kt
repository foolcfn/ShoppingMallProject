package com.cfl.network.httphelper

import android.app.Application
import android.net.UrlQuerySanitizer
import android.provider.SyncStateContract.Helpers.insert
import com.cfl.network.httphelper.monitor.db.Monitor
import com.cfl.network.httphelper.monitor.db.MonitorDatabase
import com.cfl.network.httphelper.monitor.db.MonitorPair
import com.cfl.network.httphelper.monitor.ui.MonitorNotification
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import okio.EOFException
import java.util.stream.Collector

class MonitorInterceptor: Interceptor {

	//保证在不同线程中的可见性
	@Volatile
	var isMonitorNotificationInitializeCalled = false

	override fun intercept(chain: Interceptor.Chain): Response {
		//初始化消息发送
		initNotification()
		//1.拿到request 记录其中的元素
		val request = chain.request()

		//2.将request转换成对应的monitor并插入数据库
		var monitor = insert(monitor = buildMonitor(request))

		//3.拿到response
		val response = try {
			//这里使用的还是原request
			chain.proceed(request = request)
		} catch (throwable: Throwable){
			//发生报错时以error代替Monitor

			update(monitor = monitor.copy(error = throwable.toString()))
			throw throwable
		}

		//4.将response转换成monitor,并对数据库进行更新
		monitor = processResponse()

		//5.拿到后不进行任何处理只读取monitor后更新数据
		update(monitor = monitor)
		return response
	}

	private fun processResponse(
		response: Response,
		monitor: Monitor
	): Monitor {
		val requestHeaders = response.request.headers.map{
			MonitorPair(name = it.first,)
		}


	}

	//作用：安全的初始化Notification
	private fun initNotification() {
		if (!isMonitorNotificationInitializeCalled){
			synchronized(lock = Unit) {
				if (!isMonitorNotificationInitializeCalled){
					//在这里初始化Notification,并且在多线程情况下也只初始化一次
					MonitorNotification.init(context = MyApplication.baseContext as Application)
				}
			}
		}
	}

	/**
	 * request 转换成 Monitor
	 */
	private fun buildMonitor(request: Request): Monitor{
		//对requestTime的解析
		val requestTime = System.currentTimeMillis()
		//对Url的解析
		val url = request.url
		val scheme = url.scheme
		val host = url.host
		val path = url.encodedPath
		val query = url.query ?: ""
		val method = request.method
		//拿到最原始的请求头与请求体
		val headers = request.headers
		val body = request.body
		//解析请求头
		val requestHeaders = headers.map {
			MonitorPair(name = it.first, value = it.second)
		}
		//拿到头与体后的解析过程
		val requestBody: String?
		val requestContentLength: Long
		val requestContentType: String
		//解析请求体 ： requestBody/requestContentType/requestContentLength
		if (body == null){
			requestBody = null
			requestContentType = ""
			requestContentLength = 0L
		}else{
			//双全工或一次读取无法预先加载对象，在拦截中获取会破坏内部
			requestBody = if (body.isDuplex()
				|| body.isOneShot() ||
				headers.bodyHasUnknownEncoding()
				) {
					""
				}else{
					//为什么要使用buffer进行中转 -- responseBody.body 使用的是流式数据而不是存储式数据，只能访问一次body
					//这里不仅仅进行了buffer缓存，还进行了转码


					// 原本的流程：请求体 → 网络 socket
					//body.writeTo(sink = networkSocket)

					//现在的流程：请求体 → 内存缓冲区
					//body.writeTo(sink = buffer) 写入缓冲区
					val buffer = Buffer()
					body.writeTo(sink = buffer)

					if (buffer.isProbablyUtf8()){
						//类型 -- 编码charset
						val charset = body.contentType()?.charset()?: Charsets.UTF_8
						//过程： 创建buffer -> body.writeTo(sink = buffer) 往 buffer中写入内容
						buffer.readString(charset = charset)	//使用buffer拿到请求体
					}else{
						""
					}
				}
			requestContentType = body.contentType().toString()
			requestContentLength = body.contentLength()
		}
		//为什么是更新不是插入：request与response是同一个Monitor
		return Monitor(
			id = 0L,
			url = url.toString(),
			scheme = scheme,
			host = host,
			path = path,
			query = query,
			requestTime = requestTime,
			method = method,
			requestHeaders = requestHeaders,
			requestContentType = requestContentType,
			requestContentLength = requestContentLength,
			requestBody = requestBody,
			protocol = "",
			responseHeaders = emptyList(),
			responseBody = "",
			responseContentType = "",
			responseContentLength = 0L,
			responseTime = 0L,
			responseTlsVersion = "",
			responseCipherSuite = "",
			responseMessage = "",
			error = null
		)

	}

	//判断header头中请求头的编码
	private fun Headers.bodyHasUnknownEncoding(): Boolean{
		//判断该header中是否存在"Content-Encoding"，无则返回false
		val contentEncoding = this["Content-Encoding"]?:return false
		//并且我的contentEcoding不能等于identity,也不能等于gzip
		return !contentEncoding.equals("identity", ignoreCase = true) 	//不进行大小写的字符串比对
				&& !contentEncoding.equals("gzip", ignoreCase = true)
	}

	//检查body缓存的形式是否是utf-8编码的
	private fun Buffer.isProbablyUtf8(): Boolean{
		return try {
			val prefix = Buffer()
			//拿到复制的样本数量
			val byteCount = size.coerceAtMost(maximumValue = 64) //将缓冲区的大小设置到64
			//复制样本到临时缓冲区
			copyTo(out = prefix, offset = 0, byteCount = byteCount)
			//检查前16个字符
			repeat(times = 16) {

				if (prefix.exhausted()){
					return@repeat
				}
				//读取一个Unicode字符
				val codePoint = prefix.readUtf8CodePoint()
				//去检查这个字符的协议
				if (Character.isISOControl(codePoint) && Character.isWhitespace(codePoint) ){
					return false
				}
			}
			true
		}catch (e: EOFException){
			false
		}

	}

	//插入操作
	private fun insert(monitor: Monitor): Monitor {
		val id = MonitorDatabase.instance.monitorDao().insertMonitor(monitor = monitor)
		return monitor.copy(id = id)
	}

	//数据库更新
	private fun update(monitor: Monitor){
		MonitorDatabase.instance.monitorDao().updateMonitor(monitor = monitor)
	}
}