package com.panicdev.base_bottom_navigation_mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.panicdev.fast_clien.view.other.IndicatorView
import com.panicdev.fast_clien.base.BaseApplication
import com.panicdev.fast_clien.base.BaseViewModel
import com.panicdev.kevin.common.AndroidUtilities

abstract class BaseFragment<T : ViewDataBinding, R : BaseViewModel> : Fragment(){

    lateinit var mBinding: T
    abstract val layoutResourceId : Int
    abstract val mViewModel: R

    private var progress: AlertDialog? = null
    lateinit var indicatorView: IndicatorView



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)

        initData()
        initView()
        initViewModel()
        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initProgressObserve()
        initAlertObserve()
        initToastObserve()
    }


    abstract fun initData()
    abstract fun initView()
    abstract fun initViewModel()
    abstract fun onRefresh()



    private fun initProgressObserve() {
        indicatorView =
            IndicatorView(context!!)
        mViewModel.progress.observe(this, Observer { flag ->
            when (flag.first) {
                1 -> showProgress(alpha = 0f, widthFadeIn = flag.second ?: false)
                2 -> showProgress(alpha = 1f, widthFadeIn = flag.second ?: false)
                3 -> hideProgress()
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
    fun showProgress(alpha : Float, widthFadeIn: Boolean = false) {
        indicatorView.show(backgroundAlpha = alpha, parentView = mBinding.root as ViewGroup, widthFadeIn = widthFadeIn)
    }

    fun hideProgress() {
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

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity!!).apply {
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

    /**
     * 상태바 색상변경
     *
     * <p> 상태바변경이 필요한경우 이 펑션을 사용한다.
     *
     * @param setLightIcon [true / 밝은 아이콘] [false / 어두운 아이콘]
     * @param color 배경색 id값
     */
    protected fun setStatusBar(color : Int, setLightIcon : Boolean) {
        activity!!.window.apply {
            decorView.systemUiVisibility = if (setLightIcon) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = color
        }
    }
}