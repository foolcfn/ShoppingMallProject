package com.cfl.network.httphelper.monitor.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//Bean属性
@Entity(tableName = MonitorDatabase.tableName)
class Monitor(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Long,
	@ColumnInfo(name = "url")
	val url: String,
	@ColumnInfo(name = "scheme")
	val scheme: String,
	@ColumnInfo(name = "host")
	val host: String,
	@ColumnInfo(name = "path")
	val path: String,
	@ColumnInfo(name = "query")
	val query: String,
	@ColumnInfo(name = "protocol")
	val protocol: String,
	@ColumnInfo(name = "method")
	val method: String,
	@ColumnInfo(name = "requestHeaders")
	val requestHeaders: List<MonitorPair>,
	@ColumnInfo(name = "requestBody")
	val requestBody: String?,
	@ColumnInfo(name = "requestContentType")
	val requestContentType: String,
	@ColumnInfo(name = "requestContentLength")
	val requestContentLength: Long,
	@ColumnInfo(name = "requestTime")
	val requestTime: Long,
	@ColumnInfo(name = "responseHeaders")
	val responseHeaders: List<MonitorPair>,
	@ColumnInfo(name = "responseBody")
	val responseBody: String?,
	@ColumnInfo(name = "responseContentType")
	val responseContentType: String,
	@ColumnInfo(name = "responseContentLength")
	val responseContentLength: Long,
	@ColumnInfo(name = "responseTime")
	val responseTime: Long,
	@ColumnInfo(name = "responseTlsVersion")
	val responseTlsVersion: String,
	@ColumnInfo(name = "responseCipherSuite")
	val responseCipherSuite: String,
	@ColumnInfo(name = "responseCode")
	val responseCode: Int = DEFAULT_RESPONSE_CODE,
	@ColumnInfo(name = "responseMessage")
	val responseMessage: String,
	@ColumnInfo(name = "error")
	val error: String?

): Serializable {

	//关于Models内部的一些处理逻辑

	companion object{
		private const val DEFAULT_RESPONSE_CODE = -1024
	}

	val notificationText: String = "$responseCode  $path"

	override fun equals(other: Any?): Boolean {
		if (this === other) return true  // 如果是同一个对象
		if (other !is Monitor) return false  // 如果类型不同

		return id == other.id &&
				url == other.url &&
				scheme == other.scheme &&
				host == other.host &&
				path == other.path &&
				query == other.query &&
				protocol == other.protocol &&
				method == other.method &&
				requestHeaders == other.requestHeaders &&
				requestBody == other.requestBody &&
				requestContentType == other.requestContentType &&
				requestContentLength == other.requestContentLength &&
				requestTime == other.requestTime &&
				responseHeaders == other.responseHeaders &&
				responseBody == other.responseBody &&
				responseContentType == other.responseContentType &&
				responseContentLength == other.responseContentLength &&
				responseTime == other.responseTime &&
				responseTlsVersion == other.responseTlsVersion &&
				responseCipherSuite == other.responseCipherSuite &&
				responseCode == other.responseCode &&
				responseMessage == other.responseMessage &&
				error == other.error
	}

	fun copy(
		id: Long = this.id,
		url: String = this.url,
		scheme: String = this.scheme,
		host: String = this.host,
		path: String = this.path,
		query: String = this.query,
		requestTime: Long = this.requestTime,
		method: String = this.method,
		requestHeaders: List<MonitorPair> = this.requestHeaders,
		requestContentLength: Long = this.requestContentLength,
		requestContentType: String = this.requestContentType,
		requestBody: String? = this.requestBody,
		protocol: String = this.protocol,
		responseHeaders: List<MonitorPair> = this.responseHeaders,
		responseBody: String? = this.responseBody,
		responseContentType: String = this.responseContentType,
		responseContentLength: Long = this.responseContentLength,
		responseTime: Long = this.responseTime,
		responseTlsVersion: String = this.responseTlsVersion,
		responseCipherSuite: String = this.responseCipherSuite,
		responseMessage: String = this.responseMessage,
		error: String? = this.error
	): Monitor {
		return Monitor(
			id = id,
			url = url,
			scheme = scheme,
			host = host,
			path = path,
			query = query,
			requestTime = requestTime,
			method = method,
			requestHeaders = requestHeaders,
			requestContentLength = requestContentLength,
			requestContentType = requestContentType,
			requestBody = requestBody,
			protocol = protocol,
			responseHeaders = responseHeaders,
			responseBody = responseBody,
			responseContentType = responseContentType,
			responseContentLength = responseContentLength,
			responseTime = responseTime,
			responseTlsVersion = responseTlsVersion,
			responseCipherSuite = responseCipherSuite,
			responseMessage = responseMessage,
			error = error
		)
	}

}

//关于响应头的定义  属性：内容
data class MonitorPair(
	val name: String,
	val value: String
): Serializable

//关于Monitor状态的处理
internal enum class MonitorStatus{
	Requesting,
	Complete,
	Failed
}


