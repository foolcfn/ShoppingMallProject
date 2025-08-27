package com.cfl.library_base.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cfl.library_base.bean.DataLoadState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class BaseViewModel :ViewModel(){
    private val TAG = "BaseViewModel"

    //在此处写一些ViewModel公共的信息

    //BaseFragment需要的一些属性
    var dataState:MutableLiveData<DataLoadState> = MutableLiveData<DataLoadState>()

    //进行UI的加载，在数据变化的时候进行UI的改变
    fun launchUI(
        block:suspend CoroutineScope.() -> Unit,
        fail:((e:Exception) -> Unit) ?= null,   //该属性默认为空
        state: MutableLiveData<DataLoadState>?=dataState
    ): Job {
        //一进入必进入加载页面
        state?.value = DataLoadState.LOADING
        //协程工作域启动的本质是一个Job
        return viewModelScope.launch {
             try{
                block()
                //数据加载成功 正常执行 数据状态正常
                dataState.value = DataLoadState.SUCCESS
            }catch (e:Exception) {
                fail?.invoke(e)
                 Log.i(TAG, "launchUI: " + e.message)
                 dataState.value = DataLoadState.FAIL
            }

        }
    }




}