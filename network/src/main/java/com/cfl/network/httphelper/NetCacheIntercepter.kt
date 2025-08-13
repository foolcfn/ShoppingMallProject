package com.cfl.network.httphelper

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.RequiresPermission
import okhttp3.Interceptor
import okhttp3.Response

/**
 *TODO：对部分缓存的实时性要求很高要求不启用缓存，缓存时间定为0
 * 策略：先将所有的request的缓存都禁用了，在只对特定的url开启缓存
 */
class NetCacheIntercepter : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		var request = chain.request()
		//request不进行处理
		var response = chain.proceed(request)
		return response.newBuilder()
			.header("Cache-Control", "public,max-age = 0")
			.build()
	}

}