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

    val divider = "?&od=T31"

    @SuppressLint("CheckResult")
    fun getList(
        pageNo: Int = 0,
        searchKeyword: String? = null,
        complete: (List<BoardItem>) -> Unit
    ) {
        val paramPageNo = "&po=$pageNo"
        val url = "${board.url}$divider$paramPageNo"


        Observable.fromCallable {
            Jsoup.connect(url).get()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { doc ->
                val itemList = doc.select("div.list_item.symph-row").map {
                    BoardItem(
                        title = it.select("div.list_title").select("a.list_subject").select("span")
                            .first().attributes()["title"],
                        time = it.select("div.list_time").select("span").first().text(),
                        hit = it.select("div.list_hit").select("span").first()?.text(),
                        reply = it.select("div.list_reply").select("span").first()?.text(),
                        author =it.select("span.nickname").first()?.text() ?: "empty",
                        symph = it.select("div.list_symph")?.select("span")?.first()?.text()
                    )
                }
                complete(itemList)
            }
    }
}