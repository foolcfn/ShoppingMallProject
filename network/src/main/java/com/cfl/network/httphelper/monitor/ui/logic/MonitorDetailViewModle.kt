package com.cfl.network.httphelper.monitor.ui.logic

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cfl.network.httphelper.monitor.db.Monitor
import com.cfl.network.httphelper.monitor.db.MonitorDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MonitorDetailViewModle(
	private val application: Application,
	private val monitorId: Long
) : ViewModel() {
	init {
		viewModelScope.launch {
			MonitorDatabase.instance.monitorDao().queryMonitorAsFlow(id = monitorId)
				.distinctUntilChanged()
				.collectLatest {
					//进行数据收集
				}
		}
	}
}