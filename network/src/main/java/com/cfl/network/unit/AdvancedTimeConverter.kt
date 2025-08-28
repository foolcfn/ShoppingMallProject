package com.cfl.network.unit

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cfl.network.unit.AdvancedTimeConverter.toGMTString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object AdvancedTimeConverter{


	/**
	 * 高级时间转换器，支持自定义格式和时区
	 * @param timestamp 时间戳（毫秒）
	 * @param pattern 时间格式模式
	 * @param timeZone 时区ID
	 * @param locale 地区设置
	 * @return 格式化后的时间字符串
	 */
	fun convert(timestamp: Long?, pattern: String, timeZone: String, locale: Locale): String {
		if (timestamp == null) {
			return "N/A"
		}

		return try {
			val sdf = SimpleDateFormat(pattern, locale)
			sdf.timeZone = TimeZone.getTimeZone(timeZone)
			sdf.format(Date(timestamp))
		} catch (e: Exception) {
			"格式转换错误"
		}
	}

	/**
	 * 转换为GMT格式的便捷方法
	 */
	fun toGMTFormat(timestamp: Long?): String {
		return convert(timestamp, "EEE, dd MMM yyyy HH:mm:ss 'GMT'", "GMT", Locale.ENGLISH)
	}

	/**
	 * 转换为本地时间格式
	 */
	fun toLocalFormat(timestamp: Long?): String {
		return convert(timestamp, "yyyy-MM-dd HH:mm:ss", TimeZone.getDefault().id, Locale.getDefault())
	}

	/**
	 * 扩展函数：为Long类型添加转换功能
	 */
	fun Long?.toGMTString(): String = toGMTFormat(this)
	fun Long?.toLocalString(): String = toLocalFormat(this)

}

@androidx.databinding.BindingAdapter("getTime")
fun TextView.setTime(timestamp: Long?){
	text = timestamp.toGMTString()
}
