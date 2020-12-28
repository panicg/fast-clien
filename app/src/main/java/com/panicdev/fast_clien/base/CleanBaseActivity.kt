package com.panicdev.fast_clien.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity


open class CleanBaseActivity : AppCompatActivity() {


    /**
     * 상태바 색상변경
     *
     * <p> 상태바변경이 필요한경우 이 펑션을 사용한다.
     *
     * @param setLightIcon [true / 밝은 아이콘] [false / 어두운 아이콘]
     * @param color 배경색 id값
     */
    private fun setStatusBarBlue(setLightIcon : Boolean, color : Int) {
        window.apply {
            decorView.systemUiVisibility = if (setLightIcon) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = color
        }
    }
}