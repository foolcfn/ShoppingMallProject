package com.cfl.network.httphelper.monitor.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cfl.network.R
import com.cfl.network.databinding.ActivityMonitorBinding

class MonitorActivity : AppCompatActivity() {

	lateinit var mDataBing:ActivityMonitorBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
		mDataBing = DataBindingUtil.setContentView<ActivityMonitorBinding>(this,R.layout.activity_monitor)
		mDataBing.lifecycleOwner = this
		mDataBing.executePendingBindings()

		//设置布局
		var layoutManager = LinearLayoutManager(this)
		mDataBing.recycler.layoutManager = layoutManager

		//设置分割线
		var dividerLine = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
		mDataBing.recycler.addItemDecoration(dividerLine)

		//设置适配器
		mDataBing.recycler.adapter

	}
}