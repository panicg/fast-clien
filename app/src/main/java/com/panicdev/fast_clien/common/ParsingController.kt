package com.panicdev.fast_clien.common

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import org.w3c.dom.Element
import java.lang.Exception

class ParsingController(private val board: MainBoard) {

    val divider = "&=T31"

    @SuppressLint("CheckResult")
    fun getList(pageNo: Int = 0, searchKeyword: String? = null, complete: (List<String>) -> Unit) {
        val paramPageNo = "&po=$pageNo"
        val url = "${board.url}$divider$paramPageNo"


        Observable.fromCallable {
            Jsoup.connect(url).get()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { doc ->
                val itemList = doc.select("div.list_item.symph-row").map {
                    //title
                    it.select("div.list_title").select("a.list_subject").select("span").first().attributes()["title"]
                }
                complete(itemList)
            }
    }
}