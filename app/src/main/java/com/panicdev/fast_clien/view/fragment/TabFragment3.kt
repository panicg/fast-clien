package com.panicdev.fast_clien.view.fragment

import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.base_bottom_navigation_mvvm.R
import com.panicdev.base_bottom_navigation_mvvm.databinding.FragmentTab3Binding
import com.panicdev.fast_clien.viewModel.TabViewModel3
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabFragment3 : BaseFragment<FragmentTab3Binding, TabViewModel3>() {
    override val layoutResourceId: Int = R.layout.fragment_tab_3
    override val mViewModel: TabViewModel3 by viewModel()

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initViewModel() {
    }

    override fun onRefresh() {
        toast(str = "test")
    }
}