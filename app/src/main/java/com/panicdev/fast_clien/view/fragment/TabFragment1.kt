package com.panicdev.fast_clien.view.fragment

import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.base_bottom_navigation_mvvm.databinding.FragmentTab1Binding
import com.panicdev.fast_clien.viewModel.TabViewModel1
import com.panicdev.base_bottom_navigation_mvvm.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabFragment1 : BaseFragment<FragmentTab1Binding, TabViewModel1>() {
    override val layoutResourceId: Int = R.layout.fragment_tab_1
    override val mViewModel: TabViewModel1 by viewModel()

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initViewModel() {
    }

    override fun onRefresh() {
        setStatusBar(resources.getColor(R.color.purple_200, null), true)
        mViewModel.progress()
    }
}