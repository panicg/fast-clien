package com.panicdev.fast_clien.common

import com.panicdev.fast_clien.common.ConstantData.baseUrl
import com.panicdev.fast_clien.common.ConstantData.boardUrl


object ConstantData{
    const val baseUrl : String = "https://m.clien.net"
    const val boardUrl : String = "/service/board"
}
enum class MainBoard {
    park,
    news,
    image,
    kin,
    useful,
    pds,
    lecture,
    use,
    chehum,
    jirum,
    sold,
    hongbo,
    insure;

    val title: String
        get() {
            return when (this) {
                park -> "모두의공원"
                news -> "새로운소식"
                image -> "사진게시판"
                kin -> "아무거나질문"
                useful -> "유용사이트"
                pds -> "자료실"
                lecture -> "팁과강좌"
                use -> "사용기"
                chehum -> "체험단사용기"
                jirum -> "알뜰구매"
                sold -> "회원중고장터"
                hongbo -> "직접흥보"
                insure -> "보험상담실"
            }
        }

    val url : String
    get() {
        return "$baseUrl$boardUrl/" + when (this) {
            park -> "park"
            news -> "news"
            image -> "image"
            kin -> "kin"
            useful -> "useful"
            pds -> "pds"
            lecture -> "lecture"
            use -> "use"
            chehum -> "chehum"
            jirum -> "jirum"
            sold -> "sold"
            hongbo -> "hongbo"
            insure -> "insure"
        }
    }
}

enum class SubBoard {

}