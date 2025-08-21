package com.cfl.network.httphelper.monitor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cfl.network.databinding.FragmentResponseBinding
import com.cfl.network.httphelper.monitor.ui.logic.MonitorViewModel

class ResponseFragment: Fragment() {

	companion object {

		var fragment: ResponseFragment = ResponseFragment()
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment OverviewFragment.
		 */
		// TODO: Rename and change types and number of parameters
		fun newInstance(): ResponseFragment{
			return fragment
		}

	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		var fragmentOverviewBinding = FragmentResponseBinding.inflate(inflater, container, false)
		var monitorViewModel = ViewModelProvider(requireActivity()).get(MonitorViewModel::class.java)
		fragmentOverviewBinding.data = monitorViewModel.monitor.value
		return fragmentOverviewBinding.root
	}




}