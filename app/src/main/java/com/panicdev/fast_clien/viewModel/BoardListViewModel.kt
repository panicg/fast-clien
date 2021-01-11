package com.panicdev.fast_clien.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.panicdev.fast_clien.base.BaseViewModel
import com.panicdev.fast_clien.common.MainBoard
import com.panicdev.fast_clien.common.ParsingController

class BoardListViewModel : BaseViewModel() {
    var parsingController: ParsingController = ParsingController(MainBoard.park)

    private val _list = MutableLiveData<List<String>>()
    val list: LiveData<List<String>>
        get() = _list

    init {

    }


    fun getTest(){
        parsingController.getList(0) { list ->
            _list.value = list
        }
    }
}