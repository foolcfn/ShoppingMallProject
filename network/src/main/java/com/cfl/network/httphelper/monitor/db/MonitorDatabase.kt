package com.cfl.network.httphelper.monitor.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cfl.network.httphelper.MyApplication

import java.util.concurrent.TimeUnit

@Database(entities = [Monitor::class], version = 1)
//自动实现部分类型匹配转换
@TypeConverters(value = [MonitorTypeConverter::class])
abstract class MonitorDatabase: RoomDatabase() {
	//定义DAO
	abstract fun monitorDao(): MonitorDao

	companion object{

		//数据库名称
		private const val MONITOR_DATABASE_NAME = "Monitor"
		//表名
		const val tableName = "Monitor"
		//Monitor数据库
		private var monitorDatabase: MonitorDatabase ?= null

		//单例模式创建  得到一个已经实现的单例MonitorDatabase
		val instance: MonitorDatabase
			get() {
				return monitorDatabase?:synchronized(lock = MonitorDatabase::class.java) {
					val cache = monitorDatabase
					if (cache != null){
						return@synchronized cache
					}
					val db = createDb(MyApplication.applicationContext as Application)
					return@synchronized db
				}
			}

		private fun createDb(context: Application): MonitorDatabase {
			 return Room.databaseBuilder<MonitorDatabase>(
				 context = context,
				 MonitorDatabase::class.java,
				 MONITOR_DATABASE_NAME
			 ).fallbackToDestructiveMigration()
				 .setAutoCloseTimeout(
					 autoCloseTimeout = 120,
					 autoCloseTimeUnit = TimeUnit.SECONDS
				 )
				 .build()
		}
	}

}

