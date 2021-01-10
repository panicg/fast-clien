package com.panicdev.fast_clien.common

class ParsingController(private val board : MainBoard) {

    val divider = "&=T31"


    fun getList(pageNo : Int = 0, searchKeyword : String? = null){
        val paramPageNo = "&po=$pageNo"
        val callUrl = "${board.url}$divider$paramPageNo"
    }
}