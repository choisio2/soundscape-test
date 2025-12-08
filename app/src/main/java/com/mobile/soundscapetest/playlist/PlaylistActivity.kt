package com.mobile.soundscapetest.playlist

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobile.soundscapetest.PrefFragment.mynameFragment
import com.mobile.soundscapetest.R

class PlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_playlist)

        // 프래그먼트 바로 실행
        val fragment = GallerymodeFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.playlist_fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}