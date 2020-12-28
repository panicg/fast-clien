package com.panicdev.fast_clien.view.fragment

import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.base_bottom_navigation_mvvm.R
import com.panicdev.base_bottom_navigation_mvvm.databinding.FragmentTab4Binding
import com.panicdev.fast_clien.viewModel.TabViewModel4
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabFragment4 : BaseFragment<FragmentTab4Binding, TabViewModel4>() {
    override val layoutResourceId: Int = R.layout.fragment_tab_4
    override val mViewModel: TabViewModel4 by viewModel()

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initViewModel() {
    }

    override fun onRefresh() {
        alert(message = "test")
    }
}