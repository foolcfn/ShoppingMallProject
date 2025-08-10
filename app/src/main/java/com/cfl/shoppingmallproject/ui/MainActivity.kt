package com.cfl.shoppingmallproject.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.cfl.feature_classify.ui.ClassifyFragment
import com.cfl.feature_detail.ui.DetailFragment
import com.cfl.feature_recommend.ui.ui.RecommentFragment
import com.cfl.feature_user.ui.UserFragment
import com.cfl.library_base.config.ARouterPath
import com.cfl.library_base.ui.activity.BaseActivity
import com.cfl.shoppingmallproject.BR
import com.cfl.shoppingmallproject.R
import com.cfl.shoppingmallproject.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(){
    override fun getBindingViewModelId(): Int {
        return BR.viewModel
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModle(): MainViewModel? {
        return ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun initView() {
        var recommentFragment:RecommentFragment  = ARouter.getInstance().build(ARouterPath.Recommend.FRAGMENT_RECOMMENT).navigation() as RecommentFragment
        var classifyFragment:ClassifyFragment = ARouter.getInstance().build(ARouterPath.Classify.FRAGMENT_CLASSIFY).navigation() as ClassifyFragment
        var detailFragment:DetailFragment = ARouter.getInstance().build(ARouterPath.Detail.FRAGMENT_DETAIL).navigation() as DetailFragment
        var userFragment:UserFragment = ARouter.getInstance().build(ARouterPath.User.FRAGMENT_USER).navigation() as UserFragment

        //获取Fragment管理者
        var supportFragmentManager = supportFragmentManager
        //开启事务
        var beginTransaction = supportFragmentManager.beginTransaction()
        //设置当前Fragment


    }

    override fun initData() {
    }


}