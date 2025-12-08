package com.mobile.soundscapetest.PrefFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.mobile.soundscapetest.R

class ArtistAdapter(
    private var artistList: List<ArtistData>,
    private val onItemClick: () -> Unit // [수정] Int를 넘기지 않고, 클릭됐다는 신호만 줌
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: MaterialCardView = itemView.findViewById(R.id.card_artist_image)
        val nameTextView: TextView = itemView.findViewById(R.id.tv_artist_name)

        fun bind(artist: ArtistData) {
            nameTextView.text = artist.name

            // 테두리 그리기
            if (artist.isSelected) {
                cardView.strokeWidth = 12
                cardView.strokeColor = ContextCompat.getColor(itemView.context, R.color.white)
            } else {
                cardView.strokeWidth = 0
            }

            itemView.setOnClickListener {
                // [핵심 변경] 여기서 개수를 세지 않습니다!
                // 단순히 상태만 바꾸고 프래그먼트에게 "나 클릭됐어!"라고 알립니다.

                artist.isSelected = !artist.isSelected // 토글(반전)
                notifyItemChanged(bindingAdapterPosition) // 화면 갱신

                onItemClick() // 프래그먼트 호출
            }
        }
    }

    fun updateList(newList: List<ArtistData>) {
        artistList = newList
        notifyDataSetChanged()
    }

    fun clearSelection() {
        artistList.forEach { it.isSelected = false }
        notifyDataSetChanged()
        onItemClick() // 초기화됐으니 다시 계산하라고 알림
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist_selection, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(artistList[position])
    }

    override fun getItemCount(): Int = artistList.size
}