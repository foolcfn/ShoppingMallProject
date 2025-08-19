package com.cfl.network.httphelper.monitor.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cfl.network.databinding.ItemMonitorBinding
import com.cfl.network.httphelper.monitor.db.Monitor
import com.cfl.network.httphelper.monitor.ui.MonitorAdapter.MonitorViewHolder

class MonitorAdapter : PagingDataAdapter<Monitor, MonitorViewHolder>(diffCallback) {

	lateinit var onClickEvent: OnItemClickListener

	companion object {
		private val diffCallback = object : DiffUtil.ItemCallback<Monitor>() {
			override fun areItemsTheSame(oldItem: Monitor, newItem: Monitor): Boolean {
				return oldItem.id == newItem.id // 根据唯一 ID 判断是否是同一项
			}

			override fun areContentsTheSame(oldItem: Monitor, newItem: Monitor): Boolean {
				return oldItem ==  newItem // 判断内容是否相同（需 Monitor 实现 equals()）
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonitorViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		var itemMonitorBinding = ItemMonitorBinding.inflate(inflater, parent, false)
		return MonitorViewHolder(itemMonitorBinding)
	}

	override fun onBindViewHolder(holder: MonitorViewHolder, position: Int) {
		val item = getItem(position)
		item?.let { monitor ->
			holder.bind(monitor)
		}
	}
	//注意：内部类一定要加inner，不然只是一个嵌套类
	inner class MonitorViewHolder(var itemMonitorBinding: ItemMonitorBinding) : RecyclerView.ViewHolder(itemMonitorBinding.root) {

		fun bind(monitor: Monitor) {
			// 绑定数据到 View dataBing下·不需要实现这一步，但是还要实现点击事件
			itemMonitorBinding.root.setOnClickListener {
				//存在点击回调
				onClickEvent.onThemeListClick()
			}


		}
	}
	//设置一个点击接口
	interface OnItemClickListener{
		//点击单元框会进行跳转
		fun onThemeListClick();
	}
}

