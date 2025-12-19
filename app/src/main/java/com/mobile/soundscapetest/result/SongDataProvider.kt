package com.mobile.soundscapetest.result

object SongDataProvider {
    fun createDummyData(): List<SongData> {
        val list = ArrayList<SongData>()

        // 1. Blue Valentine
        list.add(SongData(
            name = "Blue Valentine",
            artist = "NMIXX",
            albumCover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp"
        ))

        // 2. Good Goodbye
        list.add(SongData(
            name = "Good Goodbye",
            artist = "화사",
            albumCover = "https://image.bugsm.co.kr/album/images/500/41305/4130508.jpg"
        ))

        // 3. ONE MORE TIME
        list.add(SongData(
            name = "ONE MORE TIME",
            artist = "ALLDAY PROJECT",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41335/4133502.jpg?version=20251206014315"
        ))

        // 4. SPAGHETTI
        list.add(SongData(
            name = "SPAGHETTI (feat. j-hope of BTS)",
            artist = "LE SSERAFIM",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41314/4131460.jpg?version=20251028011200"
        ))

        // 5. Golden
        list.add(SongData(
            name = "Golden",
            artist = "HUNTR/X",
            albumCover = "https://image.bugsm.co.kr/album/images/1000/381763/38176338.jpg"
        ))

        // 6. 멸종위기사랑
        list.add(SongData(
            name = "멸종위기사랑",
            artist = "이찬혁",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41229/4122969.jpg?version=20250729020842"
        ))

        // 7. Drowning
        list.add(SongData(
            name = "Drowning",
            artist = "WOODZ",
            albumCover = "https://image.bugsm.co.kr/album/images/200/40839/4083984.jpg?version=20250315015832"
        ))

        // 8. FOCUS
        list.add(SongData(
            name = "FOCUS",
            artist = "Hearts2Hearts",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41289/4128980.jpg?version=20251022002830"
        ))

        // 9. 뛰어(JUMP)
        list.add(SongData(
            name = "뛰어(JUMP)",
            artist = "BLACKPINK",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41229/4122947.jpg?version=20250712005750"
        ))

        // 10. NOT CUTE ANYMORE
        list.add(SongData(
            name = "NOT CUTE ANYMORE",
            artist = "아일릿",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41342/4134249.jpg?version=20251129010313"
        ))

        // 11. XOXZ
        list.add(SongData(
            name = "XOXZ",
            artist = "IVE",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41260/4126044.jpg?version=20250828003754"
        ))

        // 12. toxic till the end
        list.add(SongData(
            name = "toxic till the end",
            artist = "로제",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41113/4111311.jpg?version=20250213023756"
        ))

        // 13. 첫 눈
        list.add(SongData(
            name = "첫 눈",
            artist = "EXO",
            albumCover = "https://image.bugsm.co.kr/album/images/200/4005/400586.jpg?version=20250107030153"
        ))

        // 14. Whiplash
        list.add(SongData(
            name = "Whiplash",
            artist = "aespa",
            albumCover = "https://image.bugsm.co.kr/album/images/200/41087/4108755.jpg?version=20241126004739"
        ))

        return list
    }
}