package com.panicdev.fast_clien.view.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.fast_clien.R
import com.panicdev.fast_clien.base.BaseActivity
import com.panicdev.fast_clien.databinding.ActivityMainBinding
import com.panicdev.fast_clien.databinding.HolderMenuBinding
import com.panicdev.fast_clien.view.fragment.BoardDetailFragment
import com.panicdev.fast_clien.view.fragment.BoardListFragment
import com.panicdev.fast_clien.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val mViewModel: MainViewModel by viewModel()
    override val layoutResourceId: Int = R.layout.activity_main


    var currentFragment: Fragment? = null

    var listFragment: BoardListFragment? = null
    var detailFragment: BoardDetailFragment? = null


    override fun setData() {
        listFragment = BoardListFragment()

    }

    override fun setView() {
        mBinding.run {
            view = this@MainActivity
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(frame.id, listFragment!!).commit()
            currentFragment = listFragment
        }
    }

    override fun setViewModel() {
        mViewModel.run {

        }
    }


    fun toDetail() {
        changeFragment(true)
    }

    fun toList() {
        changeFragment(false)
    }

    private fun changeFragment(fragment: Fragment) {
        val tranaction = supportFragmentManager.beginTransaction()
        currentFragment?.let { currentFragment ->
            tranaction.hide(currentFragment)
        }
        supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
            ?.let { findedFragment ->
                tranaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                tranaction.show(findedFragment)
                (findedFragment as BaseFragment<*, *>).onRefresh()
            } ?: run {
            tranaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            tranaction.add(
                mBinding.frame.id,
                fragment,
                fragment.javaClass.simpleName
            )
            tranaction.show(fragment)
        }
        currentFragment = fragment
        tranaction.commitAllowingStateLoss()
    }


    private fun changeFragment(toDetail: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()

        if (toDetail) {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            detailFragment?.let {
                transaction.show(it)
            } ?: run {
                detailFragment = BoardDetailFragment()
                transaction.add(mBinding.frame.id, detailFragment!!)
                transaction.show(detailFragment!!)
            }
            listFragment?.let {
                transaction.hide(it)
            }
            currentFragment = detailFragment
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)

            listFragment?.let {
                transaction.show(it)
            }
            detailFragment?.let {
                transaction.hide(it)
            }

            currentFragment = listFragment
        }
        transaction.commitAllowingStateLoss()
    }


    override fun onBackPressed() {
        when (currentFragment) {
            is BoardListFragment -> {
                val thatFragment = (currentFragment as BoardListFragment)
                if (thatFragment.sheetBehaviorBottom.state == BottomSheetBehavior.STATE_EXPANDED) {
                    thatFragment.sheetBehaviorBottom.state = BottomSheetBehavior.STATE_COLLAPSED
                } else {

                }
            }

            is BoardDetailFragment -> {
                toList()
            }
        }
    }
}
