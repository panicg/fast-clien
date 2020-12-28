package com.panicdev.fast_clien.view.fragment

import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.base_bottom_navigation_mvvm.R
import com.panicdev.base_bottom_navigation_mvvm.databinding.FragmentTab2Binding
import com.panicdev.fast_clien.viewModel.TabViewModel2
import org.koin.androidx.viewmodel.ext.android.viewModel

class TabFragment2 : BaseFragment<FragmentTab2Binding, TabViewModel2>() {
    override val layoutResourceId: Int = R.layout.fragment_tab_2
    override val mViewModel: TabViewModel2 by viewModel()

    override fun initData() {
    }

    override fun initView() {
    }

    override fun initViewModel() {
    }

    override fun onRefresh() {
        mViewModel.progress()
    }
}