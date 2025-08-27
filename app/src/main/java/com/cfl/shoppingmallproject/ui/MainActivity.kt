package com.cfl.shoppingmallproject.ui

import android.view.MotionEvent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import java.lang.ref.WeakReference

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>(){
    //创建fragment单例 -》 预加载
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
    var currentFragment:Fragment = getFragment(0)
    //当前的下标
    var currentTag:String = tag[0]

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
                R.id.rb_recomment -> swicthFragment(getFragment(0),tag[0])
                R.id.rb_classify -> swicthFragment(getFragment(1),tag[1])
                R.id.rb_detail -> swicthFragment(getFragment(2),tag[2])
                R.id.rb_user -> swicthFragment(getFragment(3),tag[3])
            }
        }
    }

    private fun swicthFragment(newFragment: Fragment, tag:String) {
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

    private fun getFragment(index:Int):Fragment =
        when(index){
            0 -> recommentFragment
            1 -> classifyFragment
            2 -> detailFragment
            else -> userFragment
        }

    override fun initData() {
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var action = event?.action
        var startX = 0F

        when(action){
            MotionEvent.ACTION_DOWN ->{
                startX = event?.getX() as Float
            }

            MotionEvent.ACTION_UP ->{
                event?.apply {
                    var endX = getX()
                    var dx = endX - startX
                    var index = tag.indexOf(currentTag)
                    var rindex = index - 1
                    var lindex = index + 1
                    //右滑前翻页
                    if (dx > 400 && rindex > 0){
                        swicthFragment(getFragment(rindex),tag[rindex])
                        return true
                    }
                    //左滑后翻页
                    if (dx < -400 && lindex < 3 ){
                        swicthFragment(getFragment(lindex),tag[lindex])
                        return true
                    }
                }

            }
        }
        return true
    }
}