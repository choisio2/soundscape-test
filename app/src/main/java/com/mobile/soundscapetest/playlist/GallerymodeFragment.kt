package com.mobile.soundscapetest.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.soundscapetest.R
import com.mobile.soundscapetest.playlist.MusicModel
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.request.RequestOptions
import com.mobile.soundscapetest.playlist.MusicDataProvider
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlin.math.abs

class GallerymodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallerymode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val musicList = MusicDataProvider.getMusicList()
        val viewPager = view.findViewById<ViewPager2>(R.id.vpGallery)
        val bgImage = view.findViewById<ImageView>(R.id.ivBlurBackground)
        val tvTitle = view.findViewById<TextView>(R.id.tvCurrentTitle)
        val tvArtist = view.findViewById<TextView>(R.id.tvCurrentArtist)




        // 1. 어댑터 연결
        val adapter = GalleryAdapter(musicList)
        viewPager.adapter = adapter

        // 2. 시작 위치 계산 (무한 스크롤 중간 지점)
        val centerPosition = Int.MAX_VALUE / 2
        val startPosition = centerPosition - (centerPosition % musicList.size)
        viewPager.setCurrentItem(startPosition, false)

        // 3. ViewPager2 설정
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        // 4. 변환 효과
        val compositePageTransformer = CompositePageTransformer()

        compositePageTransformer.addTransformer(MarginPageTransformer(20))

        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)

            val scaleFactor = 0.50f + r * 0.50f

            // [2] 비율 유지하면서 작아지게 설정
            page.scaleY = scaleFactor
            page.scaleX = scaleFactor

            // [3] 핵심: 작아지면서 생긴 빈 공간만큼 안쪽으로 당기기 (translationX)
            // 원래 크기에서 줄어든 크기를 뺀 나머지 공간의 절반만큼 이동시킴
            val myOffset = (page.width - (page.width * scaleFactor)) / 3

            if (position < 0) {
                // 왼쪽 카드는 오른쪽으로 당김 (+)
                page.translationX = myOffset
            } else {
                // 오른쪽 카드는 왼쪽으로 당김 (-)
                page.translationX = -myOffset
            }
        }

        viewPager.setPageTransformer(compositePageTransformer)

        // ==================================================================
        // [핵심 수정] 화면 갱신 로직을 함수로 분리 (중복 제거)
        // ==================================================================
        fun updateUI(position: Int) {
            val realPosition = position % musicList.size
            val currentMusic = musicList[realPosition]

            tvTitle.text = currentMusic.title
            tvArtist.text = currentMusic.artist

            // 배경 블러 처리
            context?.let { ctx ->
                Glide.with(ctx)
                    .load(currentMusic.albumCover)
                    .apply(RequestOptions.bitmapTransform(BlurTransformation(25, 3)))
                    .into(bgImage)
            }
        }

        // 5. [중요] 처음 시작할 때 강제로 한 번 실행!
        viewPager.post {
            updateUI(startPosition)
        }

        // 6. 페이지 변경 감지
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateUI(position) // 여기서도 같은 함수 호출
            }
        })
        
        
        
        // 리스트형 모드로 변경
        val btnSwitchMode = view.findViewById<ImageButton>(R.id.btnSwitchMode)
        btnSwitchMode.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.playlist_fragment_container, ListmodeFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}