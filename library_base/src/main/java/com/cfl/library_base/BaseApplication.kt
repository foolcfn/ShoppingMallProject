package com.cfl.library_base

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import me.jessyan.autosize.AutoSizeConfig

open class BaseApplication:Application() {

    //设置一个环境
    companion object{
        private lateinit var instance:Application

        fun getContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        //在调试模式下  对ARouter进行配置
        if(BuildConfig.DEBUG){
            //打开ARouter的调试模式与Log系统
            ARouter.openLog()
            ARouter.openDebug()
        }

        //初始化ARouter
        ARouter.init(this)

        //开启头条适配方案
        AutoSizeConfig.getInstance().setCustomFragment(true)
    }

}