package com.cfl.network.httphelper

import okhttp3.Cache
import java.io.File

//网络缓存帮助器
object NetWorkCacheHelper {

	//定义需要缓存的Api
	var listCacheUrl = listOf<String>(
		"/user.user/smsRegister",
		"/data.richtext"
	)

	//定义缓存时间
	const val OnLINE_CACHE_TIME = 60 * 60 * 24        //一天

	//创建缓存文件
	fun createCacheFile(): Cache {
		var file = File(MyApplication.cacheDir, "okHttpCache")
		//缓存大小
		var cacheSize = 100 * 1024 * 1024
		return Cache(file,cacheSize.toLong())
	}
}