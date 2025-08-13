package com.cfl.network.httphelper

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.annotation.RequiresPermission
import okhttp3.Interceptor
import okhttp3.Response

class ForceCacheInterceptor: Interceptor {

	@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
	override fun intercept(chain: Interceptor.Chain): Response {
		var request = chain.request()
		val urlNeedUseCache = request.url.toUrl().path in NetWorkCacheHelper.listCacheUrl
		if (urlNeedUseCache){
			//没链接上网络强制使用缓存
			if (!isOnline()){
				request = request.newBuilder()
						.header("Cache-Control",
							"public,only-if-cache,"+
						"max-stale=${NetWorkCacheHelper.OnLINE_CACHE_TIME}"
						+",must-revalidate",
							)
						.build()
			}
		}
		return chain.proceed(request)
	}


	//检查是否连接上网络
	@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
	fun isOnline(): Boolean {
		//得到网络连接的系统服务
		var connMgr =
			MyApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		// 拿到服务信息后确定网络信息 networkInfo -》 确定连接状态
		var networkInfo: NetworkInfo? = connMgr.activeNetworkInfo
		return networkInfo?.isConnected == true
	}
}