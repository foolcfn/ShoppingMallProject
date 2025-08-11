package com.cfl.network.httphelper

import android.util.Log
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

//单例静态实体
object RetrofitHelper {

	private const val TAG = "RetrofitHelper"
	const val CONNECT_TIME_OUT_SECONDS = 50 //最长连接时间
	const val READ_TIME_OUT_SECONEDS = 20 //最长读取时间
	const val WIRTE_TIME_OUT_SECONEDS = 20 //最长写入时间
	const val BASE_URL = "http://shop.fzqq.fun/addons/shopro"

	//创建okhttpClient  concurrent的时间单位，代替 DateTime
	private val okHttpClient =
		OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(20, TimeUnit.SECONDS)
			.writeTimeout(20, TimeUnit.SECONDS)
			.retryOnConnectionFailure(true)		//是否支持失败时重试
			.build()

	val retrofit: Retrofit by lazy {
		Log.i(TAG, ": 创建okhttpClient的进程名${Thread.currentThread().name}" )
		var baseUrl = BASE_URL
		//Retrofit只是okhttp的封装 传递 client
		Retrofit.Builder()
			.baseUrl(baseUrl)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())  //配置转换器
			.build()
	}

	@JvmStatic
	suspend inline fun <reified S> get():S {
		//withContext --> 用于给协程 。。。
		return withContext(Dispatcher.IO) {
			retrofit.create(S::class.java)
		}

	}
}