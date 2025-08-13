package com.cfl.network.httphelper

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//单例静态实体
object RetrofitHelper {

	private const val TAG = "RetrofitHelper"
	const val CONNECT_TIME_OUT_SECONDS = 50 //最长连接时间
	const val READ_TIME_OUT_SECONEDS = 20 //最长读取时间
	const val WIRTE_TIME_OUT_SECONEDS = 20 //最长写入时间
	const val BASE_URL = "http://shop.fzqq.fun/addons/shopro"

	//缓存思路：1.如果本地配置了.cache(NetWorkCacheHelper.createCacheFile()),并且缓存头中开启缓存，在有持久化缓存
			//2.如何本地没配置	.cache(NetWorkCacheHelper.createCacheFile())，但缓存头中开启缓存，则缓存只在该次进程中有效

	//创建okhttpClient  concurrent的时间单位，代替 DateTime
	private val okHttpClient =
		OkHttpClient.Builder()
			.connectTimeout(10, TimeUnit.SECONDS)
			.readTimeout(20, TimeUnit.SECONDS)
			.writeTimeout(20, TimeUnit.SECONDS)
			.retryOnConnectionFailure(true)		//是否支持失败时重试
			.cache(NetWorkCacheHelper.createCacheFile())
			.addNetworkInterceptor(ForceCacheInterceptor())
			.addInterceptor(NetworkInterceptor())
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

	//更方便接入为java static代码
	@JvmStatic
	suspend inline fun <reified S> get():S {
		//withContext --> 挂起函数 在某一线程池下运行的代码块
		return withContext(Dispatchers.IO) {
			retrofit.create(S::class.java)
		}
	}
}