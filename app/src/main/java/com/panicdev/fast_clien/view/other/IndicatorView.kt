package com.panicdev.fast_clien.view.other

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import com.panicdev.fast_clien.databinding.ViewIndicatorBinding

class IndicatorView : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initInternal()
    }

    lateinit var mBinding: ViewIndicatorBinding
    var parentView: ViewGroup? = null
    var widthFadeIn = false
    val duration = 500L

    private val params = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    private fun initInternal() {
        mBinding = ViewIndicatorBinding.inflate(LayoutInflater.from(context), this, true)
        isClickable = true
    }

    fun show(backgroundAlpha: Float = 1.0f, parentView: ViewGroup, widthFadeIn: Boolean = false) {
        //remove view all
        hide()
        //init
        this.parentView = parentView
        this.widthFadeIn = widthFadeIn
        parentView.addView(this, params)
        this.bringToFront()
        //start
        mBinding.run {
            bg.alpha = backgroundAlpha
            if (widthFadeIn) {
                //use fade in
                clRoot.run {
                    alpha = 0F
                    animate().apply {
                        alpha(1F)
                        duration = this@IndicatorView.duration
                        interpolator = AccelerateInterpolator()
                    }
                }
            } else {
                //not use fade in
                alpha = 1F
            }
        }
    }

    fun hide() {
        if (widthFadeIn){
            mBinding.clRoot.animate().apply {
                alpha(0F)
                duration = this@IndicatorView.duration
                this.setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        parentView?.run {
                            removeView(this@IndicatorView)
                        }
                    }
                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }
                })
            }
        } else {
            parentView?.run {
                removeView(this@IndicatorView)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}