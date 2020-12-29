package com.panicdev.fast_clien.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import com.panicdev.fast_clien.R
import com.panicdev.fast_clien.base.CleanBaseActivity
import com.panicdev.fast_clien.databinding.ActivityMainBinding
import com.panicdev.fast_clien.view.fragment.BottomMenuFragment
import com.panicdev.fast_clien.viewModel.BottomMenuViewModel

class MainActivity : CleanBaseActivity() {

    lateinit var mBinding : ActivityMainBinding

    val bottomMenuFragment = BottomMenuFragment()

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
        }
    }
}
