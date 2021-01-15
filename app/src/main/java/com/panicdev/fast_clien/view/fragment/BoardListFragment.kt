package com.panicdev.fast_clien.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.chainrefund.kevin.common.addOnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.fast_clien.databinding.FragmentBoardListBinding
import com.panicdev.fast_clien.viewModel.BoardListViewModel
import com.panicdev.fast_clien.R
import com.panicdev.fast_clien.common.BoardItem
import com.panicdev.fast_clien.common.MainBoard
import com.panicdev.kevin.common.*
import com.panicdev.fast_clien.databinding.HolderBoardListBinding
import com.panicdev.fast_clien.databinding.HolderMenuBinding
import com.panicdev.fast_clien.view.activity.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardListFragment : BaseFragment<FragmentBoardListBinding, BoardListViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_board_list
    override val mViewModel: BoardListViewModel by viewModel()

    lateinit var boardList: ArrayList<BoardItem>
    lateinit var mainMenuList: List<String>
    lateinit var subMenuList: List<String>
    lateinit var boardListAdapter: BoardListAdapter
    lateinit var mainMenuAdapter: MainMenuAdapter
    lateinit var subMenuAdapter: SubMenuAdapter

    lateinit var sheetBehaviorBottom: BottomSheetBehavior<ConstraintLayout>

    override fun initData() {
        boardList = ArrayList()
        mainMenuList = resources.getStringArray(R.array.menu_main).toList()
        subMenuList = resources.getStringArray(R.array.menu_sub).toList()

        mainMenuAdapter = MainMenuAdapter()
        subMenuAdapter = SubMenuAdapter()
    }

    override fun initView() {
        mBinding.run {
            rvList.run {
                boardListAdapter = BoardListAdapter()
                mBinding.rvList.run {
                    adapter = boardListAdapter
                }
                addOnItemClickListener<ListViewHolder> { _, position ->
                    (activity as MainActivity).toDetail(boardList[position])
                }

                addOnScrollListener(object :RecyclerView.OnScrollListener(){
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (!rvList.canScrollVertically(-1)) {
                        } else if (!rvList.canScrollVertically(1)) {
                            mViewModel.run{
                                initBoard(MainBoard.park)
                                reqBoard(true)
                            }
                        } else {
                        }
                    }
                })
            }

            toolbar.run {
                title = "클리앙"
            }

            swipeRefresh.run {
                setOnRefreshListener {
                    boardList.clear()
                    mViewModel.run {
                        initBoard(MainBoard.park)
                        reqBoard(false)
                    }
                }
            }

            //하단 시트
            sheetBehaviorBottom = BottomSheetBehavior.from(bottomSheet.bottomSheet).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) {
                    }

                    override fun onStateChanged(p0: View, p1: Int) {
                        when (p1) {
                            BottomSheetBehavior.STATE_EXPANDED -> {
                            }
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                //if collapsed state, scroll up to top
                                mBinding.bottomSheet.scrollview.smoothScrollTo(0, 0)
                            }
                        }
                    }
                })
            }

            bottomSheet.run {
                rvMenuMain.run {
                    addOnItemClickListener<MainMenuViewHolder> { viewHolder, position ->
                        when(position){
                            0 -> {
                                mViewModel.run {
                                    boardList.clear()
                                    initBoard(MainBoard.park)
                                    reqBoard(false)
                                }

                            }

                            3 -> {
                                mViewModel.run {
                                    boardList.clear()
                                    initBoard(MainBoard.news)
                                    reqBoard(false)
                                }
                            }
                        }
                    }
                }
                mainMenuAdapter = MainMenuAdapter()
                subMenuAdapter = SubMenuAdapter()
                rvMenuMain.adapter = mainMenuAdapter
                rvMenuSub.adapter = subMenuAdapter
            }
        }
    }

    override fun initViewModel() {
        mViewModel.run {
            list.observe(this@BoardListFragment, Observer {
                mBinding.swipeRefresh.isRefreshing = false
                boardList.addAll(it)
                boardListAdapter.notifyDataSetChanged()
            })

            initBoard(MainBoard.news)
            reqBoard(false)
        }
    }

    override fun onRefresh() {

    }




    inner class BoardListAdapter : RecyclerView.Adapter<ListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.holder_board_list, parent, false)
            return ListViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            holder.initView(boardList[position])
        }

        override fun getItemCount(): Int = boardList.size

    }

    inner class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mBinding = HolderBoardListBinding.bind(view)
        fun initView(item: BoardItem) {
            mBinding.run {
                tvTitle.text = item.title
                tvReply.text = item.reply
                tvAuthor.text = item.author
                tvSymph.text = item.symph
                tvTime.text = item.time
            }
        }
    }


    inner class MainMenuAdapter : RecyclerView.Adapter<MainMenuViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.holder_menu, parent, false)
            return MainMenuViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
            holder.initView(mainMenuList[position])
        }

        override fun getItemCount(): Int = mainMenuList.size

    }

    inner class SubMenuAdapter : RecyclerView.Adapter<MainMenuViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.holder_menu, parent, false)
            return MainMenuViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
            holder.initView(subMenuList[position])
        }

        override fun getItemCount(): Int = subMenuList.size

    }

    inner class MainMenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mBinding = HolderMenuBinding.bind(view)
        fun initView(menu: String) {
            mBinding.run {
                tvMenu.text = menu
            }
        }
    }
}