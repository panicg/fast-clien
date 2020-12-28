package com.panicdev.fast_clien.base

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.panicdev.fast_clien.view.other.CustomToolbar
import com.panicdev.fast_clien.view.other.IndicatorView
import com.panicdev.kevin.common.AndroidUtilities


abstract class BaseActivity<T : ViewDataBinding, R : BaseViewModel> : CleanBaseActivity() {

    lateinit var mBinding: T
    abstract val mViewModel: R
    abstract val layoutResourceId: Int
    lateinit var indicatorView: IndicatorView

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView<T>(this, resId) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, layoutResourceId)
        initProgressObserve()
        initAlertObserve()
        initToastObserve()
        setData()
        setView()
        setViewModel()
    }

    //데이터 세팅
    abstract fun setData()

    //뷰 세팅
    abstract fun setView()

    //뷰모델 세팅
    abstract fun setViewModel()

    private fun initProgressObserve() {
        indicatorView = IndicatorView(this)
        mViewModel.progress.observe(this, Observer { flag ->
            when (flag.first) {
                1 -> {
                    showProgress(0f, flag.second ?: false)
                }
                2 -> {
                    showProgress(1f, flag.second ?: false)
                }
                3 -> {
                    hideProgress()
                }
            }
        })
    }

    private fun initAlertObserve() {
        mViewModel.alert.observe(this, Observer { message ->
            alert(message = message)
        })
    }

    private fun initToastObserve() {
        mViewModel.alert.observe(this, Observer { message ->
            toast(str = message)
        })
    }

    fun setToolbar(toolbar: CustomToolbar) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun showProgress(alpha: Float, widthFadeIn: Boolean = false) {
        indicatorView.show(alpha, mBinding.root as ViewGroup, widthFadeIn)
    }

    private fun hideProgress() {
        indicatorView.hide()
    }


    protected fun toast(str: String, longLen: Boolean? = false) {
        Toast.makeText(
            BaseApplication.context,
            str,
            longLen?.let { if (longLen) Toast.LENGTH_LONG else Toast.LENGTH_SHORT }
                ?: Toast.LENGTH_SHORT).show()
    }

    protected fun alert(
        title: String = "",
        message: String = "",
        isChoice: Boolean = false,
        isCancelable: Boolean = false,
        completion: ((isConfirm: Boolean) -> Unit)? = null
    ) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setCancelable(isCancelable)
            if (isChoice) {
                setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                    completion?.invoke(false)
                }
            }
            setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                completion?.invoke(true)
            }
        }
        AndroidUtilities.runOnUIThread({
            builder.show()
        })
    }
}