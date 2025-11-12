package com.mobile.soundscapetest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobile.soundscapetest.com.mobile.soundscapetest.DataModel


class RVAdapter(val items: MutableList<DataModel>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RVAdapter.ViewHolder {
        // 이 'listview_item' 레이아웃에는 커버, 제목, 가수를 표시할 뷰가 모두 있어야 합니다.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RVAdapter.ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        // 5. listview_item.xml에 있는 3개의 뷰를 ID로 찾아서 변수에 저장
        private val coverView = itemView.findViewById<ImageView>(R.id.rvCover)
        private val titleView = itemView.findViewById<TextView>(R.id.rvTitle)
        private val singerView = itemView.findViewById<TextView>(R.id.rvSinger)

        // 6. 'item: String'을 'item: DataModel'로 변경
        fun bindItems(item: DataModel){
            // 7. DataModel의 데이터를 3개의 뷰에 각각 설정
            titleView.text = item.title
            singerView.text = item.singer

            // 8. 앨범 커버(String)를 ImageView에 표시 (Glide 라이브러리 사용 예시)
            // (Spotify ImagesApi를 써야 한다면 로직이 조금 더 복잡해집니다)
            Glide.with(itemView.context)
                .load(item.cover) // item.cover가 이미지 URL 문자열이라고 가정
                .placeholder(R.drawable.ic_launcher_foreground) // ⚠️ 로딩 중 기본 이미지
                .into(coverView)
        }
    }


}