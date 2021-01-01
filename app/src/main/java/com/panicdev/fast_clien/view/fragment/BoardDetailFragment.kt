package com.panicdev.fast_clien.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.fast_clien.databinding.FragmentBoardListBinding
import com.panicdev.fast_clien.viewModel.BoardListViewModel
import com.panicdev.fast_clien.R
import com.panicdev.fast_clien.databinding.FragmentBoardDetailBinding
import com.panicdev.fast_clien.databinding.HolderBoardListBinding
import com.panicdev.fast_clien.databinding.HolderMenuBinding
import com.panicdev.fast_clien.view.activity.MainActivity
import com.panicdev.fast_clien.viewModel.BoardDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardDetailFragment : BaseFragment<FragmentBoardDetailBinding, BoardDetailViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_board_detail
    override val mViewModel: BoardDetailViewModel by viewModel()


    override fun initData() {
    }

    override fun initView() {
        mBinding.run{
        }
    }

    override fun initViewModel() {
        mViewModel.run {

        }
    }

    override fun onRefresh() {

    }

}