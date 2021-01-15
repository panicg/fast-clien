package com.panicdev.fast_clien.common

import java.util.*

data class BoardItem(
    //게시판
    val boardType : MainBoard,
    //제목
    val title: String,
    //댓글 수
    val reply: String?,
    //작성일
    val time: String,
    //조회수
    val hit: String?,
    //공감수
    val symph: String?,
    //작성자
    val author: String,
    //디테일 링크
    val onclick : String
)