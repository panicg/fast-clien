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

    //하단 메뉴 프래그먼트
    private val boardListFragment = BoardListFragment()
    lateinit var sheetBehaviorBottom: BottomSheetBehavior<ConstraintLayout>

    lateinit var fragmentTransactionList: FragmentTransaction

    //메인메뉴 리스트
    lateinit var mainMenuList: List<String>
    lateinit var subMenuList: List<String>
    lateinit var rvAdapterMain: MainMenuAdapter
    lateinit var rvAdapterSub: SubMenuAdapter

    var currentFragment: Fragment? = null


    override fun setData() {
        mainMenuList = resources.getStringArray(R.array.menu_main).toList()
        subMenuList = resources.getStringArray(R.array.menu_sub).toList()
    }

    override fun setView() {
        mBinding.run {
            view = this@MainActivity

            fragmentTransactionList = supportFragmentManager.beginTransaction().apply {
                replace(frame.id, boardListFragment)
                commit()
            }

            toolbar.run {
                title = "클리앙"
            }

            //하단 시트
            sheetBehaviorBottom = BottomSheetBehavior.from(bottomSheet.bottomSheet).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) {
                    }

                    override fun onStateChanged(p0: View, p1: Int) {
                        when (p1) {
                            BottomSheetBehavior.STATE_EXPANDED -> {
                            }
                            BottomSheetBehavior.STATE_HIDDEN -> {
                            }
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                            }
                        }
                    }
                })
            }

            bottomSheet.run {
                rvAdapterMain = MainMenuAdapter()
                rvAdapterSub = SubMenuAdapter()
                rvMenuMain.adapter = rvAdapterMain
                rvMenuSub.adapter = rvAdapterSub
            }

        }
    }

    override fun setViewModel() {
        mViewModel.run {

        }
    }

    fun replacePage(){
        changeFragment(BoardDetailFragment())
    }

    private fun changeFragment(fragment: Fragment) {
        val tranaction = supportFragmentManager.beginTransaction()
        currentFragment?.let { currentFragment ->
            tranaction.hide(currentFragment)
        }
        supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)
            ?.let { findedFragment ->
                tranaction.show(findedFragment)
                (findedFragment as BaseFragment<*, *>).onRefresh()
                currentFragment = findedFragment
            } ?: run {
            tranaction.add(
                mBinding.frame.id,
                fragment,
                fragment.javaClass.simpleName
            )
            tranaction.show(fragment)
            currentFragment = fragment
        }
        tranaction.commitAllowingStateLoss()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onBackPressed() {

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
