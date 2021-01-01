package com.panicdev.fast_clien.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chainrefund.kevin.common.addOnItemClickListener
import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.fast_clien.databinding.FragmentBoardListBinding
import com.panicdev.fast_clien.viewModel.BoardListViewModel
import com.panicdev.fast_clien.R
import com.panicdev.kevin.common.*
import com.panicdev.fast_clien.databinding.HolderBoardListBinding
import com.panicdev.fast_clien.view.activity.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardListFragment : BaseFragment<FragmentBoardListBinding, BoardListViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_board_list
    override val mViewModel: BoardListViewModel by viewModel()

    lateinit var subMenuList: List<String>
    lateinit var boardListAdapter: BoardListAdapter

    override fun initData() {
        subMenuList = resources.getStringArray(R.array.menu_sub).toList()
        boardListAdapter = BoardListAdapter()
    }

    override fun initView() {
        mBinding.run{
            rvList.run {
                adapter = boardListAdapter
                addOnItemClickListener<ListViewHolder> { viewHolder, position ->
                    (activity as MainActivity).replacePage()
                }
            }
        }
    }

    override fun initViewModel() {
        mViewModel.run {

        }
    }

    override fun onRefresh() {

    }


    inner class BoardListAdapter : RecyclerView.Adapter<ListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.holder_board_list, parent, false)
            return ListViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            holder.initView(subMenuList[position])
        }

        override fun getItemCount(): Int = subMenuList.size

    }

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mBinding = HolderBoardListBinding.bind(view)
        fun initView(title: String) {
            mBinding.run {
                tvTitle.text = title
            }
        }
    }

}