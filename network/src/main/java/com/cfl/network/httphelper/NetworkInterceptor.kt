package com.cfl.network.httphelper

import android.R.attr.path
import android.text.TextUtils
import com.cfl.network.bean.BaseBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.IOException
import java.nio.charset.Charset

//网络拦截器 对网络模块的处理
class NetworkInterceptor: Interceptor {
	//一次性加载这些内容就不能使用flow
	val shopUrlList =
		listOf<String>()

	override fun intercept(chain: Interceptor.Chain): Response {
		//拿到上一chain处理好的request
		var request = chain.request()
		//Interceptor常规思路：对request的处理
		request = onResquestChange(request,chain)
		var response = chain.proceed(request)
		//思路：下面是对response的处理
		response = onResponseChange(response,chain)
		return response

	}


	private fun onResquestChange(
		request: Request,
		chain: Interceptor.Chain
	): Request{
		//进行这一链的request处理
		var requestBuild = request.newBuilder()
		//进行公共属性的配置，例如header token cookie等
		for (header: Map.Entry<String, String?> in MyHttpConfig.getGlobalHeaders().entries){
			if (header.value != null){
				requestBuild.addHeader(header.key,header.value!!)
			}
		}
//		//当前路径是否在列表中
//		var containPath =
//			shopUrlList.any{ path ->		//any 任一
//				request.url.toString().contains(path)
//			}
//		//如果在列表中就给头添加一个标头
//		if (containPath){
//			//添加
//			requestBuild.addHeader()
//		}
		return requestBuild.build()
	}

	private fun onResponseChange(
		response: Response,
		chain: Interceptor.Chain
	): Response {
		if (response.isSuccessful) {
			//source大文件数据流
			var source = response.body?.source()
			source?.request(Long.MAX_VALUE)
			var buffer = source?.buffer
			//创建缓冲区副本（缓冲区不受影响），并按照utf-8的形式进行解码
			var body = buffer?.clone()?.readString(Charset.forName("UTF-8"))

			//服务器返回成功 但是Body为空
			if (TextUtils.isEmpty(body) && emptyRspCode(response.code)) {
				throw IOException("server response error")
			}
			//报这两个码值则追加内容  （注意区分：response的码值 与 response中code码值的区别，前者是网络协议的码值，后者是自定义的码值）
			if (emptyRspCode(response.code)) {
				return response
					.newBuilder()
					.body(
						ResponseBody.create(
							"application/json; charset=utf-8".toMediaTypeOrNull(), body ?: ""
						)
					).build()
			}

			val bean: BaseBean<*> =
				Gson().fromJson(body, object : TypeToken<BaseBean<*>>() {}.type)
			if (bean.code == 200) {
				runCatching {
					return response.newBuilder()
						.body(
							ResponseBody.create(
								"application/json; charset=utf-8".toMediaTypeOrNull(),
								body ?: ""
							)
						).build()
				}.onFailure {
					throw IOException("ApiException : response:$response,bean:$bean")
				}
			}
		}
		return response
	}
	private fun emptyRspCode(code:Int): Boolean{
		return code == 204 || code == 205
	}
}