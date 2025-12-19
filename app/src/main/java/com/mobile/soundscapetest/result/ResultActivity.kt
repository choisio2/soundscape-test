package com.mobile.soundscapetest.result

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mobile.soundscapetest.R
import com.mobile.soundscapetest.databinding.ActivityResultBinding
import com.mobile.soundscapetest.result.SongDataProvider

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var adapter: PlaylistResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 데이터 생성 (여기에 사진 URL을 직접 넣으세요)
        val dummyList = SongDataProvider.createDummyData()

        // 2. 리사이클러뷰 설정
        setupRecyclerView(dummyList)

        // 3. 상단 헤더 이미지 설정 (자동으로 dummyList의 앞 4개를 가져옴)
        setupHeaderImages(dummyList)
    }


    private fun setupRecyclerView(songList: List<SongData>) {
        adapter = PlaylistResultAdapter(songList) {
            // 푸터 클릭 시 바텀시트
            showRegenerateBottomSheet()
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            this.adapter = this@ResultActivity.adapter
        }
    }

    private fun setupHeaderImages(songList: List<SongData>) {
        // XML에 있는 4개의 이미지뷰 ID (ivCover1 ~ ivCover4)
        val headerImageViews = listOf(
            binding.ivCover1,
            binding.ivCover2,
            binding.ivCover3,
            binding.ivCover4
        )

        // 리스트에서 앞에서부터 순서대로 이미지를 꺼내 헤더에 넣음
        for (i in headerImageViews.indices) {
            if (i < songList.size) {
                // 각 ImageView에 URL 로드
                loadUrlToImageView(headerImageViews[i], songList[i].albumCover)
            }
        }

        // 배경 그라데이션 이미지 (첫 번째 곡의 커버를 배경으로 사용)
        if (songList.isNotEmpty()) {
            loadUrlToImageView(binding.ivBackgroundGradient, songList[0].albumCover)
        }
    }

    // --- 이미지 로드 헬퍼 함수 ---
    private fun loadUrlToImageView(imageView: ImageView, url: String) {
        if (url.isNotEmpty()) {
            Glide.with(this)
                .load(url)
                .transform(CenterCrop()) // 꽉 채우기
                .into(imageView)
        } else {
            // URL이 비어있을 때 보여줄 기본 색상
            imageView.setImageResource(R.color.black)
        }
    }

    private fun showRegenerateBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_regenerate, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }
}