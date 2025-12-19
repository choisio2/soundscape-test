package com.mobile.soundscapetest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobile.soundscapetest.PrefFragment.mynameFragment
import com.mobile.soundscapetest.playlist.PlaylistActivity
import com.mobile.soundscapetest.result.ResultActivity
import java.io.IOException
import kotlin.jvm.java
import kotlin.math.log10

class MainActivity : AppCompatActivity() {

    // UI 요소
    private lateinit var decibelText: TextView
    private lateinit var measureButton: Button

    // 데시벨 측정 관련
    private var mediaRecorder: MediaRecorder? = null
    private var isMeasuring = false

    // 주기적인 업데이트를 위한 Handler
    private val handler = Handler(Looper.getMainLooper())
    private val updateIntervalMs: Long = 300 // 0.3초마다 업데이트

    // 권한 요청 런처
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startMeasuring()
            } else {
                decibelText.text = "마이크 권한이 필요합니다."
            }
        }

    // 데시벨을 주기적으로 업데이트하는 작업
    private val updateDecibelTask = object : Runnable {
        override fun run() {
            if (isMeasuring) {
                val amplitude = mediaRecorder?.maxAmplitude ?: 0
                val db = amplitudeToDb(amplitude.toDouble())
                decibelText.text = String.format("%.2f dB", db)
                handler.postDelayed(this, updateIntervalMs)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()


        decibelText = findViewById(R.id.decibelText)
        measureButton = findViewById(R.id.measureButton)

        measureButton.setOnClickListener {
            if (isMeasuring) {
                stopMeasuring()
            } else {
                when (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)) {
                    PackageManager.PERMISSION_GRANTED -> {
                        startMeasuring()
                    }
                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }
            }
        }

        // --- 리스트형/갤러리형 페이지로 이동 ---
        val movetoRV = findViewById<Button>(R.id.moveListBtn)
        movetoRV.setOnClickListener {
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }

        // --- 추천결과 페이지로 이동 ---
        val movetoResult = findViewById<Button>(R.id.moveResultBtn)
        movetoResult.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }


        /* ----- 프래그먼트로 이동 ----- */
        val movetoFragment = findViewById<Button>(R.id.moveFragment)

        movetoFragment.setOnClickListener {
            // 1. 이동할 프래그먼트 객체 생성
            val fragment = mynameFragment()

            // 2. 프래그먼트 매니저를 통해 트랜잭션 시작
            val transaction = supportFragmentManager.beginTransaction()

            // 3. R.id.fragment_container 영역을 fragment로 교체(replace)
            transaction.replace(R.id.fragment_container, fragment)

            // (선택사항) 뒤로가기 버튼 누르면 다시 돌아오게 하려면 아래 줄 추가
            transaction.addToBackStack(null)

            // 4. 적용
            transaction.commit()
        }

    }


    private fun startMeasuring() {
        // 앱 내부 캐시 디렉토리에 임시 파일 경로 지정 -> 항상 쓰기 권한이 보장
        val outputFile = "${cacheDir.absolutePath}/temp_audio.3gp"

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(outputFile) // 이렇게 해야 캐시 오류 안남

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("MediaRecorder", "prepare() failed: ${e.message}")
                decibelText.text = "측정 준비 실패"
                // 실패 시 자원 즉시 해제
                mediaRecorder?.release()
                mediaRecorder = null
                return
            }

            try {
                start()
                isMeasuring = true
                measureButton.text = "측정 중지"
                decibelText.text = "측정 중..."
                handler.post(updateDecibelTask)
            } catch (e: Exception) { // RuntimeException 또는 IllegalStateException
                Log.e("MediaRecorder", "start() failed: ${e.message}")
                decibelText.text = "측정 시작 실패 (Error: ${e.message})"
                // start가 실패했으므로 stop()을 부르지 않고 release()만 호출
                mediaRecorder?.release()
                mediaRecorder = null
                isMeasuring = false
            }
        }
    }

    private fun stopMeasuring() {
        if (!isMeasuring && mediaRecorder == null) {
            return
        }

        handler.removeCallbacks(updateDecibelTask)

        mediaRecorder?.apply {
            try {
                stop()
            } catch (e: IllegalStateException) {
                Log.e("MediaRecorder", "stop() failed: ${e.message}")
            }
            try {
                release()
            } catch (e: IllegalStateException) {
                Log.e("MediaRecorder", "release() failed: ${e.message}")
            }
        }
        mediaRecorder = null
        isMeasuring = false
        measureButton.text = "데시벨 측정"

        if (decibelText.text.toString().contains("측정")) {
            decibelText.text = "측정이 중지되었습니다."
        }
    }

    private fun amplitudeToDb(amplitude: Double): Double {
        if (amplitude > 0) {
            return 20.0 * log10(amplitude)
        }
        return 0.0
    }

    override fun onStop() {
        super.onStop()
        stopMeasuring()
    }
}