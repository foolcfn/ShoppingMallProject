package com.cfl.network.httphelper.monitor.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MonitorDao {

	//插入操作
	@Insert
	fun insertMonitor(monitor: Monitor): Long

	//更新操作
	@Update
	fun updateMonitor(monitor: Monitor)

	//查询操作
	@Query("select * from ${MonitorDatabase.tableName} where id =:id")
	suspend fun queryMonitor(id: Long): Monitor

	//查询对应id并转做Flow
	@Query("select * from ${MonitorDatabase.tableName} where id =:id")
	fun queryMonitorAsFlow(id:Long): Flow<Monitor>

	//加载请求按照id降序并且限制加载数量
	@Query("select * from ${MonitorDatabase.tableName} order by id desc limit :limit")
	fun queryMonitors(limit:Int): Flow<List<Monitor>>

	//分页加载 返回加载数据
	@Query("select * from ${MonitorDatabase.tableName} order by id desc")
	fun queryMonitors(): PagingSource<Int, Monitor>

	//删除所有的文件列表
	@Query("delete from ${MonitorDatabase.tableName}")
	suspend fun deleteAll()
}