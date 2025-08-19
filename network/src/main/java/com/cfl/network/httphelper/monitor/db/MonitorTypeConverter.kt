package com.cfl.network.httphelper.monitor.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//实现非基本数据类型转换
class MonitorTypeConverter {

	//使用json将数据转换成字符串类型
	@TypeConverter
	fun fromJson(json: String): List<MonitorPair>{
		return Gson().fromJson<List<MonitorPair>>(json, object : TypeToken<List<MonitorPair>>(){}.type)
	}

	@TypeConverter
	//json转换成类
	fun toJson(list:List<MonitorPair>): String{
		return Gson().toJson(list)
	}


}