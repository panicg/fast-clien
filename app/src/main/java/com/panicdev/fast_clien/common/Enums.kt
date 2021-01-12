package com.panicdev.fast_clien.common

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
        return when (this) {
            park -> "https://m.clien.net/service/board/park"
            news -> "https://m.clien.net/service/board/news"
            image -> "https://m.clien.net/service/board/image"
            kin -> "https://m.clien.net/service/board/kin"
            useful -> "https://m.clien.net/service/board/useful"
            pds -> "https://m.clien.net/service/board/pds"
            lecture -> "https://m.clien.net/service/board/lecture"
            use -> "https://m.clien.net/service/board/use"
            chehum -> "https://m.clien.net/service/board/chehum"
            jirum -> "https://m.clien.net/service/board/jirum"
            sold -> "https://m.clien.net/service/board/sold"
            hongbo -> "https://m.clien.net/service/board/hongbo"
            insure -> "https://m.clien.net/service/board/insure"
        }
    }
}

enum class SubBoard {

}