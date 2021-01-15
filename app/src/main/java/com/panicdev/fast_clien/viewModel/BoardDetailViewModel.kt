package com.panicdev.fast_clien.viewModel

import com.panicdev.fast_clien.base.BaseViewModel
import com.panicdev.fast_clien.common.BoardItem
import com.panicdev.fast_clien.common.MainBoard
import com.panicdev.fast_clien.common.ParsingController

class BoardDetailViewModel : BaseViewModel() {
    var parsingController: ParsingController? = null

    var boardItem: BoardItem? = null

    fun initBoard(boardItem: BoardItem) {
        parsingController = ParsingController(boardItem)
    }

    fun reqBoard() {
        boardItem?.let {
            parsingController?.getDetail(it)
        } ?: run {
            _alert.value = "plz set board type first"
        }
    }
}