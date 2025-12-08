package com.mobile.soundscapetest.playlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.soundscapetest.R


class GallerymodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // XML 레이아웃을 메모리에 올림
        return inflater.inflate(R.layout.fragment_gallerymode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 화면 전환
        val btnSwitchMode = view.findViewById<ImageButton>(R.id.btnSwitchMode)
        btnSwitchMode.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.playlist_fragment_container, ListmodeFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}