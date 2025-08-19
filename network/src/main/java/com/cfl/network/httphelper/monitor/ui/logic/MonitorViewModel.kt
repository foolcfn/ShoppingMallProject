package com.cfl.network.httphelper.monitor.ui.logic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cfl.network.httphelper.monitor.db.Monitor
import com.cfl.network.httphelper.monitor.db.MonitorDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MonitorViewModel : ViewModel() {

	//viewModel进行数据加载，得到分页数据
	@OptIn(ExperimentalPagingApi::class)		//实验性Api
	fun getMonitors(): Flow<PagingData<Monitor>> =
		Pager(
			//配置分票信息
			config = PagingConfig(pageSize = 20,
				initialLoadSize = 30,
				prefetchDistance = 10,
				enablePlaceholders = false),
			//分页信息源
			pagingSourceFactory = {
				MonitorDatabase.instance.monitorDao().queryMonitors()
			},
			//远程中间人
			remoteMediator = null
		).flow	//转换成flow

	//点击删除功能
	fun onclickClear(){
		viewModelScope.launch {
			MonitorDatabase.instance.monitorDao().deleteAll()
		}
	}






}