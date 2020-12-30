package com.panicdev.fast_clien.view.activity

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.panicdev.fast_clien.R
import com.panicdev.fast_clien.base.CleanBaseActivity
import com.panicdev.fast_clien.databinding.ActivityMainBinding
import com.panicdev.fast_clien.view.fragment.BottomMenuFragment
import com.panicdev.fast_clien.viewModel.BottomMenuViewModel

class MainActivity : CleanBaseActivity() {

    lateinit var mBinding : ActivityMainBinding

    //하단 메뉴 프래그먼트
    private val bottomMenuFragment = BottomMenuFragment()
    lateinit var sheetBehaviorBottom: BottomSheetBehavior<ConstraintLayout>

    lateinit var fragmentTransaction: FragmentTransaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.run {
            view = this@MainActivity
            fragmentTransaction = supportFragmentManager.beginTransaction().apply {
                replace(bottomSheet.frame.id, bottomMenuFragment)
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
                            BottomSheetBehavior.STATE_EXPANDED ->{}
                            BottomSheetBehavior.STATE_HIDDEN -> {}
                            BottomSheetBehavior.STATE_COLLAPSED -> {}
                        }
                    }
                })
            }
        }
    }
}
