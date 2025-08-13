package com.cfl.network.httphelper

import okhttp3.Interceptor
import okhttp3.Response


class MonitorInterceptor: Interceptor {

	//保证在不同线程中的可见性
	@Volatile
	var isMonitorNotificationInitializeCalled = false

	override fun intercept(chain: Interceptor.Chain): Response {
		initNotification()
		//记得更改
		return chain.proceed(chain.request())

	}

	private fun initNotification() {
		TODO("Not yet implemented")
	}
}