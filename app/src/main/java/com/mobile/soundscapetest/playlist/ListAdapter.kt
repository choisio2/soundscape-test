package com.mobile.soundscapetest.playlist

import com.mobile.soundscapetest.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val musicList: List<MusicModel>) : RecyclerView.Adapter<ListAdapter.MusicViewHolder>() {

    // 뷰홀더 클래스: 아이템 레이아웃의 요소들을 잡아두는 역할
    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val artist: TextView = itemView.findViewById(R.id.tvArtist)
        val cover: ImageView = itemView.findViewById(R.id.ivAlbumCover)
    }

    // 레이아웃 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listmode, parent, false)
        return MusicViewHolder(view)
    }

    // 데이터 연결
    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.title.text = music.title
        holder.artist.text = music.artist

        // [수정] Glide를 사용하여 URL 이미지 로드
        com.bumptech.glide.Glide.with(holder.itemView.context)
            .load(music.albumCover) // 로드할 URL
            .placeholder(R.drawable.ic_launcher_foreground) // 로딩 중에 보여줄 이미지 (선택)
            .error(R.drawable.ic_launcher_background) // 에러 났을 때 보여줄 이미지 (선택)
            .into(holder.cover) // 이미지를 넣을 뷰
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}