package com.cfl.shoppingmallproject.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
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
    //创建fragment单例
    val recommentFragment:RecommentFragment by lazy {
        ARouter.getInstance().build(ARouterPath.Recommend.FRAGMENT_RECOMMENT)
            .navigation() as RecommentFragment
    }
    val classifyFragment:ClassifyFragment by lazy {
        ARouter.getInstance().build(ARouterPath.Classify.FRAGMENT_CLASSIFY)
            .navigation() as ClassifyFragment
    }
    val detailFragment:DetailFragment by lazy {
        ARouter.getInstance().build(ARouterPath.Detail.FRAGMENT_DETAIL)
            .navigation() as DetailFragment
    }
    val userFragment:UserFragment by lazy {
        ARouter.getInstance().build(ARouterPath.User.FRAGMENT_USER)
            .navigation() as UserFragment
    }
    val tag:List<String> = listOf(
        "Recomment",
        "Classify",
        "Detail",
        "User"
        )

    //设置当前Fragment,默认recommentFragment
    var currentFragment:Fragment = recommentFragment

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

        //获取Fragment管理者
        var fragmentManager = supportFragmentManager
        //开启事务
        var beginTransaction = fragmentManager.beginTransaction()
        beginTransaction.add(R.id.fragment_view,currentFragment,"recomment")
        mDataBinding?.rgNativgtion?.setOnCheckedChangeListener{group,checkId->
            when(checkId){
                R.id.rb_recomment -> swicthFragment(recommentFragment,tag[0])
                R.id.rb_classify -> swicthFragment(classifyFragment,tag[1])
                R.id.rb_user -> swicthFragment(userFragment,tag[3])
                R.id.rb_detail -> swicthFragment(detailFragment,tag[2])
            }
        }
    }

    private fun swicthFragment(newFragment: Fragment,tag:String) {
        supportFragmentManager.beginTransaction().apply {
            if (newFragment === currentFragment){
                //立即执行，避免异步导致的错误 ，同步
                commitNow()
                return
            }
            //否则 隐藏当前的Fragment
            currentFragment?.let { hide(it) }
            //查找若无则添加当前fragement
            var taggleFragment =
                supportFragmentManager.findFragmentByTag(tag)?:
                newFragment.let { add(R.id.fragment_view,newFragment,tag) }
            //显示目标tag
            show(taggleFragment as Fragment)
            //设置当前fragment
            currentFragment = taggleFragment
            commitNow()

        }
    }

    override fun initData() {
    }

    //左滑 右滑
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

}