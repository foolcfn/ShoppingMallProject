package com.cfl.network.httphelper

object MyHttpConfig {

	//配置域名head的配置
	@JvmStatic
	fun getGlobalHeaders():Map<String, String?>{
		return mapOf(
			"User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
			"Accept" to "application/json",
			"Accept-Language" to "en-US,en;q=0.9",
			"content-type" to "application/json; charset=utf-8",
			"Cache-Control" to "public,max-age=600,immutable",
			"Connection" to "keep-alive"
		)
	}
}