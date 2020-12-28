package com.panicdev.fast_clien.viewModel

import com.panicdev.fast_clien.base.BaseViewModel
import com.panicdev.kevin.common.AndroidUtilities

class TabViewModel1 : BaseViewModel() {

    fun progress(){
        showProgress(widthFadeIn = true)
        AndroidUtilities.runOnUIThread({
            hideProgress()
        }, 2000)
    }
}