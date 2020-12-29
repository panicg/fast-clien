package com.panicdev.fast_clien.view.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.panicdev.base_bottom_navigation_mvvm.base.BaseFragment
import com.panicdev.fast_clien.R
import com.panicdev.fast_clien.databinding.FragmentMenuBinding
import com.panicdev.fast_clien.databinding.HolderMenuBinding
import com.panicdev.fast_clien.viewModel.BottomMenuViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomMenuFragment : BaseFragment<FragmentMenuBinding, BottomMenuViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_menu
    override val mViewModel: BottomMenuViewModel by viewModel()

    //메인메뉴 리스트
    lateinit var mainMenuList : List<String>
    lateinit var rvAdapter : MainMenuAdapter

    override fun initData() {
        mainMenuList = resources.getStringArray(R.array.menu_main).toList()
    }

    override fun initView() {
        mBinding.run {
            rvAdapter = MainMenuAdapter()
            rvMenu.adapter = rvAdapter
        }
    }

    override fun initViewModel() {
        mViewModel.run {

        }
    }

    override fun onRefresh() {

    }


    inner class MainMenuAdapter : RecyclerView.Adapter<MainMenuViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_menu, parent, false)
            return MainMenuViewHolder(view)
        }

        override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
            holder.initView(mainMenuList[position])
        }

        override fun getItemCount(): Int = mainMenuList.size

    }

    inner class MainMenuViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var mBinding = HolderMenuBinding.bind(view)
        fun initView(menu : String){
            mBinding.run {
                tvMenu.text = menu
            }
        }
    }
}