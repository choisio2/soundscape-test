package com.mobile.soundscapetest.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.soundscapetest.R

class ListmodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML 레이아웃을 메모리에 올림
        return inflater.inflate(R.layout.fragment_listmode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 리사이클러뷰 설정
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = ListAdapter(MusicDataProvider.getMusicList()) // 아까 만든 더미데이터 연결

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context) // 세로 리스트 형태

        // 2. 화면 전환 버튼 설정 (왼쪽 상단 회색 버튼)
        val btnSwitchMode = view.findViewById<ImageButton>(R.id.btnSwitchMode)
        btnSwitchMode.setOnClickListener {
            // GalleryModeFragment로 이동하는 코드
            // (GalleryModeFragment가 아직 안 만들어졌다면 빨간줄이 뜰 수 있음)
            parentFragmentManager.beginTransaction()
                .replace(R.id.playlist_fragment_container, GallerymodeFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}