package com.panicdev.fast_clien.common

import java.util.*

data class BoardItem(
    //제목
    val title: String,
    //댓글 수
    val commentCount: Int,
    //작성일
    val date: Date,
    //조회수
    val hit: Long,
    //공감수
    val like: Int,
    //작성자
    val writerId: String
)