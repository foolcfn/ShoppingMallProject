package com.cfl.library_base.config

import androidx.fragment.app.Fragment

//单例类型
object ARouterPath {

    object Main{
        private const val MAIN:String = "/main"
        //Activity对应的页面
        const val ACTIVITY_MIAN:String = MAIN + "/mainActivity"

    }

    object Detail{
        private const val Detail:String = "/detail"
        //detail对应的页面
        const val FRAGMENT_DETAIL:String = Detail + "/detailFragment"
    }

    object Recommend{
        private const val Recomment:String = "/recomment"
        //recommmend对应页面
        const val FRAGMENT_RECOMMENT:String = Recomment + "/recommentFragment"
    }

    object User{
        private const val User:String = "/user"
        //useer对应的页面
        const val FRAGMENT_USER:String = User + "/userFragment"
    }

    object Classify{
        private const val Classify:String = "/classify"
        //classify对应页面
        const val FRAGMENT_CLASSIFY:String = Classify + "/classifyFragment"
    }
}