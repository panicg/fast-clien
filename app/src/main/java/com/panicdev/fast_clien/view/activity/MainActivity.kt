package com.panicdev.fast_clien.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.panicdev.base_bottom_navigation_mvvm.R
import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.fast_clien.base.CleanBaseActivity
import com.panicdev.base_bottom_navigation_mvvm.databinding.ActivityMainBinding
import com.panicdev.fast_clien.view.fragment.TabFragment1
import com.panicdev.fast_clien.view.fragment.TabFragment2
import com.panicdev.fast_clien.view.fragment.TabFragment3
import com.panicdev.fast_clien.view.fragment.TabFragment4

class MainActivity : CleanBaseActivity() {

    lateinit var mBinding: ActivityMainBinding
    var backKeyPressedTime: Long = 0
    var tabFragment1: TabFragment1? = null
    var tabFragment2: TabFragment2? = null
    var tabFragment3: TabFragment3? = null
    var tabFragment4: TabFragment4? = null
    var currentFragment: BaseFragment<*, *>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.view = this
        initTab()
    }

    private fun initTab() {

        mBinding.run {
            changePage(getFragment(0))
            tab.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.action_tab_refund -> changePage(getFragment(0))
                    R.id.action_tab_receipt -> changePage(getFragment(1))
                    R.id.action_tab_shopping -> changePage(getFragment(2))
                    R.id.action_tab_more -> changePage(getFragment(3))
                }

                return@setOnNavigationItemSelectedListener true
            }
        }
    }


    private fun getFragment(index: Int): BaseFragment<*, *> {
        lateinit var fragment: BaseFragment<*, *>
        when (index) {
            0 -> tabFragment1?.let {
                fragment = it
            } ?: run {
                tabFragment1 =
                    TabFragment1()
                fragment = tabFragment1!!
            }
            1 -> tabFragment2?.let {
                fragment = it
            } ?: run {
                tabFragment2 =
                    TabFragment2()
                fragment = tabFragment2!!
            }
            2 -> tabFragment3?.let {
                fragment = it
            } ?: run {
                tabFragment3 =
                    TabFragment3()
                fragment = tabFragment3!!
            }
            3 -> tabFragment4?.let {
                fragment = it
            } ?: run {
                tabFragment4 =
                    TabFragment4()
                fragment = tabFragment4!!
            }
        }

        return fragment
    }


    private fun changePage(
        fragment: BaseFragment<*, *>,
        completion: ((fragment: BaseFragment<*, *>) -> Unit)? = null
    ) {

        val tranaction = supportFragmentManager.beginTransaction()
        currentFragment?.let {
            if (currentFragment == fragment) {
                completion?.invoke(fragment)
                return@changePage
            }
            tabFragment1?.let {
                tranaction.hide(tabFragment1 as Fragment)
            }
            tabFragment2?.let {
                tranaction.hide(tabFragment2 as Fragment)
            }
            tabFragment3?.let {
                tranaction.hide(tabFragment3 as Fragment)
            }
            tabFragment4?.let {
                tranaction.hide(tabFragment4 as Fragment)
            }
        }

        supportFragmentManager.findFragmentByTag(fragment.javaClass.name)?.let {
            tranaction.show(it)
            (it as? BaseFragment<*, *>)?.onRefresh()
        } ?: run {
            tranaction.add(mBinding.frame.id, fragment, fragment.javaClass.name)
            tranaction.show(fragment)
        }

        this.currentFragment = fragment
        tranaction.commitAllowingStateLoss()
        completion?.invoke(fragment)
    }



    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "앱 종료 안내", Toast.LENGTH_SHORT).show()
        } else {
            finishApp()
        }
    }

    private fun finishApp() {
        super.onBackPressed()
        finish()
    }


}
