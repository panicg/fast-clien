package com.panicdev.fast_clien.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.panicdev.fast_clien.base.BaseViewModel
import com.panicdev.fast_clien.common.BoardItem
import com.panicdev.fast_clien.common.MainBoard
import com.panicdev.fast_clien.common.ParsingController

class BoardListViewModel : BaseViewModel() {
    var parsingController: ParsingController = ParsingController(MainBoard.park)

    private val _list = MutableLiveData<List<BoardItem>>()
    val list: LiveData<List<BoardItem>>
        get() = _list

    var page = 0

    init {

    }


    fun getTest(isPageUp : Boolean = false){
        if (isPageUp){
            page++
        } else {
            page = 0
        }
        parsingController.getList(page) { list ->
            _list.value = list
        }
    }
}