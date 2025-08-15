package com.cfl.network.httphelper.monitor.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cfl.network.databinding.ItemMonitorBinding

class MonitorAdapter(itemView: View) : RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder>() {

//	var datas:List<>


	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): MonitorViewHolder {
		var inflater = LayoutInflater.from(parent.context)
		val contentView = ItemMonitorBinding.inflate(inflater,parent,true)
		return MonitorViewHolder(contentView.root)
	}

	override fun onBindViewHolder(
		holder: MonitorViewHolder,
		position: Int
	) {

	}

	override fun getItemCount(): Int {
		return 3
	}


	class MonitorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){


	}


}