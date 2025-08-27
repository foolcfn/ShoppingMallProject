package com.cfl.library_base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.cfl.library_base.R
import com.cfl.library_base.bean.DataLoadState
import com.cfl.library_base.databinding.ActivityErrorBinding
import com.cfl.library_base.ui.BaseViewModel

abstract class BaseFragment<V:ViewDataBinding,VM:BaseViewModel>(): Fragment() {

    protected lateinit var mViewModel:VM
    protected lateinit var mDataBinding:V
    protected lateinit var errorView:ActivityErrorBinding
    private lateinit var mProgressBar:ProgressBar
    private lateinit var root:ViewGroup
    private lateinit var errorRoot:ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mDataBinding = DataBindingUtil.inflate(inflater,getLayoutResId(),container,false)
        mDataBinding.lifecycleOwner = this

        var viewModel:VM = getViewModel()
        if (viewModel != null){
            mViewModel = viewModel
        }

        //将viewModel与DataBinding进行绑定
        var bindingVariableId:Int = getVariableId()
        if (bindingVariableId != 0){
            mDataBinding.setVariable(bindingVariableId,mViewModel)
            //进行实时数据更新
            mDataBinding.executePendingBindings()
        }
        dataObserve()
        //加载错误页面
        addloadingView()
        addloadFailView()
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    /**
     * 根据状态加载不同的页面
     */
    fun dataObserve(){
        //进行数据观测更新
        mViewModel.dataState.observe(requireActivity()){state ->
            when(state){
                DataLoadState.LOADING -> {
                    mProgressBar.visibility = ViewGroup.VISIBLE
                }
                DataLoadState.SUCCESS -> {
                    mProgressBar.visibility = ViewGroup.GONE

                }
                DataLoadState.FAIL -> {
                    mProgressBar.visibility = ViewGroup.GONE
                    mDataBinding.root.visibility = ViewGroup.VISIBLE
                }
            }
        }
    }

    /**
     * 将出现网络错误的页面加载到布局中
     */
    fun addloadFailView(){
        root = mDataBinding.root as ConstraintLayout
        //拿到错误状态提示布局
        var mActivityError =  DataBindingUtil.inflate<ActivityErrorBinding>(getLayoutInflater(), R.layout.activity_error,
            null,false)
        errorRoot = mActivityError.root as ConstraintLayout
        var layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        errorRoot.setLayoutParams(layoutParams)
        //添加错误页面
        root.addView(errorRoot)

    }

    /**
     * 创建加载中的页面
     */
    fun addloadingView(){
        //进度条样式
        mProgressBar = ProgressBar(context)

        if(mDataBinding.root is ConstraintLayout){
            //内部类layoutParams -》 设置宽高布局与位置 在 代码中
            var layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID
            //LayoutParams是在ConstraintLayout下的布局参数，如果给mProgressBar设置layoutparams说明其在ConstrainLayout下
            mProgressBar.layoutParams = layoutParams
            mProgressBar.visibility = View.GONE //默认不可见
            root = mDataBinding.root as ConstraintLayout
            //将设置好的mProgressBar添加到ConstrainLayout布局中
            root.addView(mProgressBar)
        }
    }



    /**
     * 关于数据的加载引动关于页面状态的改变
     */
    abstract fun loadData()

    abstract fun getVariableId(): Int

    abstract fun getViewModel(): VM

    abstract fun getLayoutResId(): Int
}