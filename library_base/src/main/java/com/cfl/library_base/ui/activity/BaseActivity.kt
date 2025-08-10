package com.cfl.library_base.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cfl.library_base.ui.BaseViewModel

abstract class BaseActivity<V:ViewDataBinding,VM: BaseViewModel> : AppCompatActivity() {

    var mViewModle:VM ?= null
    var mDataBinding:V ?= null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        initViewModel()
        initDataBinding()
    }

    /**
     * 在这个方法中为ViewModel设置观察者回调
     */
    fun initViewModel(){
        mViewModle = getViewModle()

        //对BaseViewModle中的值进行观察者回调

    }

    fun initDataBinding(){
        //通过DataBindingUtil.setContentView将mDataBing实体化
        mDataBinding = DataBindingUtil.setContentView<V>(this, getLayoutResId())
        mDataBinding?.apply {
            //绑定Activity生命周期
            lifecycleOwner = this@BaseActivity
            //绑定一个ViewModel,有些不与Viewmodel关联
            setVariable(getBindingViewModelId(),mViewModle)
            //不用等待下一帧，立刻刷新界面 --》 类似自定义View中的刷新
            executePendingBindings()
        }
    }

    abstract fun getBindingViewModelId(): Int

    abstract fun getLayoutResId(): Int

    abstract fun getViewModle(): VM?

    abstract fun initView();    //在Activity中对View进行初始化 包括宽高可见性点击事件等一切有关View的变化 尤其注意关于View的观察者回调

    abstract fun initData();    //对数据进行初始化，尤其是观察者回调

}