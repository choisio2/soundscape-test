package com.mobile.soundscapetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobile.soundscapetest.com.mobile.soundscapetest.DataModel
import com.mobile.soundscapetest.databinding.ActivityRclistBinding

class RclistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRclistBinding
    private val songList = mutableListOf<DataModel>()
    private lateinit var adapter: RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRclistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 뉴진스 노래 10곡으로 songList를 채웁니다.
        loadNewJeansSongs()

        // 2. 어댑터(RVAdapter)를 생성합니다.
        adapter = RVAdapter(songList)

        // 3. 리사이클러뷰(songRV)에 어댑터와 레이아웃 매니저를 설정합니다.
        binding.songRV.adapter = adapter
        binding.songRV.layoutManager = LinearLayoutManager(this)
    }
    
    // 리스트 생성
    private fun loadNewJeansSongs() {
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
        songList.add(
            DataModel(
                cover = "https://i.namu.wiki/i/bH8QAtP6ft_iRUrN-BtoeFn3luR8WEu8KDeR6sdbc-onsH8h6QoUQkAUTr--INMIILYTMrMEiDVr45rN9ojBVA.webp",
                title = "Blue Valentine",
                singer = "NMIXX",
                trackUri = "spotify:track:3S8FzC3m6tB3FqbnLpT8oR"
            )
        )
    }
}