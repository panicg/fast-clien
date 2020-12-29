package com.panicdev.fast_clien.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.panicdev.fast_clien.R
import com.panicdev.fast_clien.base.CleanBaseActivity
import com.panicdev.fast_clien.databinding.ActivityMainBinding

class MainActivity : CleanBaseActivity() {

    lateinit var mBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.run {
            view = this@MainActivity

        }
    }
}
