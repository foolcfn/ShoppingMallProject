package com.cfl.network.httphelper.monitor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cfl.network.R

class RequestFragment: Fragment() {



	companion object {

		var fragment: RequestFragment= RequestFragment()
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @param param1 Parameter 1.
		 * @param param2 Parameter 2.
		 * @return A new instance of fragment OverviewFragment.
		 */
		// TODO: Rename and change types and number of parameters
		fun newInstance(): RequestFragment{
			return fragment
		}

	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_overview, container, false)
	}
}