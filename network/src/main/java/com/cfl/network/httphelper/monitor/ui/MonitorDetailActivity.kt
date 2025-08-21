package com.cfl.network.httphelper.monitor.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.cfl.network.BR
import com.cfl.network.R
import com.cfl.network.databinding.ActivityMonitorBinding
import com.cfl.network.databinding.ActivityMonitorDetailBinding
import com.cfl.network.httphelper.monitor.db.Monitor
import com.cfl.network.httphelper.monitor.ui.logic.MonitorDetailViewModle
import com.cfl.network.httphelper.monitor.ui.logic.MonitorViewModel
import java.lang.ref.WeakReference

class MonitorDetailActivity : AppCompatActivity() {

	lateinit var mDataBinding: ActivityMonitorDetailBinding
	lateinit var mViewModel: MonitorViewModel
	lateinit var monitor: Monitor

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		var fragments = mutableListOf< Fragment>()

		var overviewFragment = OverviewFragment.newInstance()
		fragments.add(overviewFragment)
		var requestFragment = RequestFragment.newInstance()
		fragments.add(requestFragment)
		var responseFragment = ResponseFragment()
		fragments.add(responseFragment)


		//设置mDataBinding
		mDataBinding = DataBindingUtil.setContentView<ActivityMonitorDetailBinding>(
			this,
			R.layout.activity_monitor_detail
		)
		mDataBinding.lifecycleOwner = this
		mDataBinding.executePendingBindings()
		mViewModel = ViewModelProvider(this).get(MonitorViewModel::class.java)
		//得到Monitor传递来的数据
		monitor = intent.getSerializableExtra<Monitor>("monitor", Monitor::class.java)!!
		//给ViewModel中的数传值
		mViewModel.monitor.value = monitor
		//设置data数据
		mDataBinding.data = monitor

		mDataBinding.vp.adapter = object : FragmentStateAdapter (this){
			override fun createFragment(position: Int): Fragment {
				return fragments[position]
			}

			override fun getItemCount(): Int {
				return fragments.size
			}
		}

	}
}