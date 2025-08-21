package com.cfl.network.httphelper.monitor.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cfl.network.BR
import com.cfl.network.R
import com.cfl.network.databinding.ActivityMonitorBinding
import com.cfl.network.httphelper.monitor.db.Monitor
import com.cfl.network.httphelper.monitor.ui.logic.MonitorViewModel
import kotlinx.coroutines.launch

class MonitorActivity : AppCompatActivity(), MonitorAdapter.OnItemClickListener {

	lateinit var mDataBing:ActivityMonitorBinding
	lateinit var mViewModel: MonitorViewModel

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
		mViewModel = ViewModelProvider(this).get(MonitorViewModel::class.java)
		//拿到viewModel
		mDataBing.setVariable(BR.viewModel, mViewModel)

		//设置布局
		var layoutManager = LinearLayoutManager(this)
		mDataBing.recycler.layoutManager = layoutManager

		//设置分割线
		var dividerLine = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
		mDataBing.recycler.addItemDecoration(dividerLine)

		//设置适配器
		mDataBing.recycler.adapter = MonitorAdapter()

		//进行数据的设置
		lifecycleScope.launch {
			//flow流拿到的数据会集中在流中，只有当collect收集时才会启动
			mViewModel.getMonitors().collect { pagingData ->
				var monitorAdapter: MonitorAdapter = mDataBing.recycler.adapter as MonitorAdapter
				//提交数据(会触发数据库更新)
				monitorAdapter.submitData(pagingData)
			}
		}
	}

	override fun onThemeListClick(monitor: Monitor) {
		var intent = Intent(this, MonitorDetailActivity::class.java)
		intent.putExtra("monitor", monitor)
		intent.putExtra("monitor_id",monitor.id)
		//跳转到相应的细节页面
		startActivity(intent)
	}
}