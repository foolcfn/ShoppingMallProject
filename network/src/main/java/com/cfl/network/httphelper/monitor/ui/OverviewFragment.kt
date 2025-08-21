package com.cfl.network.httphelper.monitor.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.cfl.network.R
import com.cfl.network.databinding.FragmentOverviewBinding
import com.cfl.network.httphelper.monitor.ui.logic.MonitorViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OverviewFragment : Fragment() {


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		var fragmentOverviewBinding = FragmentOverviewBinding.inflate(inflater,container,false)

		var monitorViewModel = ViewModelProvider(requireActivity()).get(MonitorViewModel::class.java)
		fragmentOverviewBinding.data = monitorViewModel.monitor.value
		return fragmentOverviewBinding.root
	}

	companion object {

		var fragment: OverviewFragment = OverviewFragment()
		fun newInstance(): OverviewFragment{
			return fragment
		}

	}
}