package com.mobile.soundscapetest

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobile.soundscapetest.databinding.ActivitySpotifyBinding

// Spotify 관련
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class SpotifyActivity : AppCompatActivity() {

    // -----------------------------------------------------------------------------------------
    // ⚠️ 1. 설정 필수: Spotify 대시보드에서 발급받은 값으로 변경
    // -----------------------------------------------------------------------------------------
    companion object {
        private const val CLIENT_ID = "11f5dcb42f4c4ae2a5f84ea6081abea5" // ⚠️ 본인의 Client ID로 변경
        private const val REDIRECT_URI = "com.mobile.soundscapetest://callback" // ⚠️ 본인의 Redirect URI로 변경
        private const val TAG = "SpotifyActivity"
    }

    // ------------------com.mobile.soundscapetest://callback-----------------------------------------------------------------------
    // 2. Spotify App Remote 및 UI 바인딩 변수
    // -----------------------------------------------------------------------------------------
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private lateinit var binding: ActivitySpotifyBinding // ⚠️ ViewBinding

    // 현재 플레이어 상태 구독 (UI 업데이트용)
    private var playerStateSubscription: Subscription<PlayerState>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivitySpotifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 초기 UI 상태 설정 (연결 전)
        updateUi(isConnected = false)

        // 클릭 리스너 설정
        setupClickListeners()
    }

    override fun onStop() {
        super.onStop()
        // 앱이 정지될 때 Spotify 연결 해제
        disconnect()
    }

    // -----------------------------------------------------------------------------------------
    // 3. 클릭 리스너 설정
    // -----------------------------------------------------------------------------------------
    private fun setupClickListeners() {
        // 스포티파이 연결 버튼
        binding.btnConnectSpotify.setOnClickListener {
            connect()
        }

        // 스포티파이 연결 해제 버튼
        binding.btnDisconnectSpotify.setOnClickListener {
            disconnect()
        }

        // 재생 버튼 (일시정지 상태에서 재생 or 새 트랙 재생)
        binding.buttonPlay.setOnClickListener {
            onPlayPauseClicked()
        }

        // 일시정지 버튼
        binding.buttonPause.setOnClickListener {
            onPlayPauseClicked()
        }

        // 다음 곡 버튼
        binding.buttonSkipNext.setOnClickListener {
            onSkipNextClicked()
        }
    }

    // -----------------------------------------------------------------------------------------
    // 4. Spotify 연결/해제 로직 (샘플 코드에서 가져옴)
    // -----------------------------------------------------------------------------------------

    /** Spotify App Remote에 연결 시도 */
    private fun connect() {
        // 연결 시도 중 UI 업데이트
        binding.btnConnectSpotify.text = "연결 중..."
        binding.btnConnectSpotify.isEnabled = false

        // 이전에 연결된 것이 있다면 해제
        SpotifyAppRemote.disconnect(spotifyAppRemote)

        // 코루틴으로 비동기 연결 시도
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // connectionParams 설정
                val connectionParams = ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true) // 인증 화면 표시
                    .build()

                // Spotify App Remote 연결 (suspend 함수 사용)
                spotifyAppRemote = connectToAppRemote(connectionParams)

                // 연결 성공
                logMessage("Spotify에 성공적으로 연결되었습니다.")
                onConnected()

            } catch (error: Throwable) {
                // 연결 실패
                logError(error)
                onDisconnected()
            }
        }
    }

    /** Spotify App Remote 연결 (Suspend 함수) */
    private suspend fun connectToAppRemote(connectionParams: ConnectionParams): SpotifyAppRemote =
        suspendCoroutine { cont: Continuation<SpotifyAppRemote> ->
            SpotifyAppRemote.connect(
                application,
                connectionParams,
                object : Connector.ConnectionListener {
                    override fun onConnected(remote: SpotifyAppRemote) {
                        cont.resume(remote) // 성공 시 SpotifyAppRemote 객체 반환
                    }
                    override fun onFailure(error: Throwable) {
                        cont.resumeWithException(error) // 실패 시 예외 발생
                    }
                })
        }

    /** Spotify App Remote 연결 해제 */
    private fun disconnect() {
        SpotifyAppRemote.disconnect(spotifyAppRemote)
        onDisconnected()
    }

    /** 연결 성공 시 호출 */
    private fun onConnected() {
        // UI 상태를 '연결됨'으로 업데이트
        updateUi(isConnected = true)
        // 플레이어 상태 구독 시작 (UI 자동 업데이트용)
        subscribeToPlayerState()
    }

    /** 연결 해제 또는 실패 시 호출 */
    private fun onDisconnected() {
        // 플레이어 상태 구독 취소
        playerStateSubscription?.cancel()
        playerStateSubscription = null
        spotifyAppRemote = null

        // UI 상태를 '연결 끊김'으로 업데이트
        updateUi(isConnected = false)
    }

    // -----------------------------------------------------------------------------------------
    // 5. 플레이어 제어 (핵심 기능)
    // -----------------------------------------------------------------------------------------

    /** 재생/일시정지 버튼 클릭 시 */
    private fun onPlayPauseClicked() {
        val remote = assertAppRemoteConnected() ?: return

        // 현재 플레이어 상태를 가져와서 분기 처리
        remote.playerApi.playerState.setResultCallback { playerState ->
            if (playerState.isPaused) {
                // 1. 일시정지 상태 -> 재생
                remote.playerApi.resume()
                    .setResultCallback { logMessage("음악을 다시 재생합니다.") }
                    .setErrorCallback(::logError)
            } else if (playerState.track != null) {
                // 2. 재생 중 상태 -> 일시정지
                remote.playerApi.pause()
                    .setResultCallback { logMessage("음악을 일시정지합니다.") }
                    .setErrorCallback(::logError)
            } else {
                // 3. 아무것도 재생 중이 아닐 때 -> EditText의 트랙 재생
                playTrackFromInput()
            }
        }
    }

    /** 다음 곡 버튼 클릭 시 */
    private fun onSkipNextClicked() {
        val remote = assertAppRemoteConnected() ?: return
        remote.playerApi.skipNext()
            .setResultCallback { logMessage("다음 곡으로 건너뜁니다.") }
            .setErrorCallback(::logError)
    }

    /** EditText에 입력된 URI로 트랙 재생 */
    private fun playTrackFromInput() {
        val remote = assertAppRemoteConnected() ?: return
        val trackUri = binding.editTextTrackUri.text.toString()

        if (trackUri.isBlank() || !trackUri.startsWith("spotify:track:")) {
            logMessage("유효한 Spotify 트랙 URI를 입력하세요.")
            return
        }

        remote.playerApi.play(trackUri)
            .setResultCallback { logMessage("트랙 재생을 시작합니다: $trackUri") }
            .setErrorCallback(::logError)
    }

    // -----------------------------------------------------------------------------------------
    // 6. UI 업데이트 로직
    // -----------------------------------------------------------------------------------------

    /** 플레이어 상태 구독 및 UI 업데이트 콜백 설정 */
    private fun subscribeToPlayerState() {
        val remote = assertAppRemoteConnected() ?: return

        // 기존 구독이 있다면 취소
        playerStateSubscription?.cancel()

        // 플레이어 상태 구독
        playerStateSubscription = remote.playerApi
            .subscribeToPlayerState()
            .setEventCallback { playerState ->
                // 플레이어 상태가 변경될 때마다 UI 업데이트
                updateTrackInfo(playerState.track)
                updatePlayPauseButtons(playerState.isPaused)
                updateAlbumCover(playerState.track?.imageUri)
            }
            .setErrorCallback(::logError) as Subscription<PlayerState>
    }

    /** 연결 상태에 따라 전체 UI 가시성 업데이트 */
    private fun updateUi(isConnected: Boolean) {
        // 연결 버튼
        binding.btnConnectSpotify.apply {
            visibility = if (isConnected) View.GONE else View.VISIBLE
            text = "Spotify 연결"
            isEnabled = true
        }
        // 연결 해제 버튼
        binding.btnDisconnectSpotify.visibility = if (isConnected) View.VISIBLE else View.GONE

        // --- 연결되어야만 보이는 UI 요소들 ---
        val visibility = if (isConnected) View.VISIBLE else View.GONE
        binding.imageViewAlbumCover.visibility = visibility
        binding.textViewTrackTitle.visibility = visibility
        binding.textViewArtistName.visibility = visibility
        binding.playbackControlsLayout.visibility = visibility
        binding.editTextTrackUri.visibility = visibility

        // 연결이 끊기면 플레이스홀더 이미지로 복원
        if (!isConnected) {
            binding.imageViewAlbumCover.setImageResource(R.drawable.ic_launcher_foreground) // ⚠️ 기본 이미지로 변경
            binding.textViewTrackTitle.text = "연결 대기 중"
            binding.textViewArtistName.text = "..."
        }
    }

    /** 트랙 정보(제목, 아티스트) 업데이트 */
    private fun updateTrackInfo(track: Track?) {
        if (track != null) {
            binding.textViewTrackTitle.text = track.name
            binding.textViewArtistName.text = track.artist.name
        } else {
            binding.textViewTrackTitle.text = "재생 중인 트랙 없음"
            binding.textViewArtistName.text = "..."
        }
    }

    /** 재생/일시정지 버튼 상태 업데이트 */
    private fun updatePlayPauseButtons(isPaused: Boolean) {
        if (isPaused) {
            binding.buttonPlay.visibility = View.VISIBLE
            binding.buttonPause.visibility = View.GONE
        } else {
            binding.buttonPlay.visibility = View.GONE
            binding.buttonPause.visibility = View.VISIBLE
        }
    }


    /** 앨범 커버 이미지 업데이트 (Spotify ImagesApi 사용) */
    private fun updateAlbumCover(imageUri: ImageUri?) {
        // App Remote가 연결되어 있는지 먼저 확인
        val remote = spotifyAppRemote
        if (remote == null) {
            binding.imageViewAlbumCover.setImageResource(R.drawable.ic_launcher_foreground) // ⚠️ 기본 이미지
            return
        }

        if (imageUri != null) {
            // Glide 대신 Spotify의 ImagesApi를 사용
            remote.imagesApi
                .getImage(imageUri, com.spotify.protocol.types.Image.Dimension.LARGE) // LARGE 사이즈로 요청
                .setResultCallback { bitmap ->
                    // 성공하면 비트맵을 이미지뷰에 설정
                    binding.imageViewAlbumCover.setImageBitmap(bitmap)
                }
                .setErrorCallback { throwable ->
                    // 실패하면 로그 찍고 기본 이미지로
                    logError(throwable)
                    binding.imageViewAlbumCover.setImageResource(R.drawable.ic_launcher_foreground) // ⚠️ 기본 이미지
                }
        } else {
            // 기본 이미지 표시
            binding.imageViewAlbumCover.setImageResource(R.drawable.ic_launcher_foreground) // ⚠️ 기본 이미지
        }
    }

    // -----------------------------------------------------------------------------------------
    // 7. 유틸리티 함수 (샘플 코드에서 가져옴)
    // -----------------------------------------------------------------------------------------

    /** App Remote가 연결되었는지 확인하고 반환 (아니면 null 반환 및 로그) */
    private fun assertAppRemoteConnected(): SpotifyAppRemote? {
        spotifyAppRemote?.let {
            if (it.isConnected) {
                return it
            }
        }
        logMessage("SpotifyAppRemote가 연결되지 않았습니다.")
        onDisconnected() // 연결이 끊긴 것으로 간주하고 UI 초기화
        return null
    }

    /** 간단한 토스트 메시지 및 로그 출력 */
    private fun logMessage(msg: String) {
        Log.d(TAG, msg)
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    /** 오류 로그 및 토스트 메시지 출력 */
    private fun logError(throwable: Throwable) {
        Log.e(TAG, "오류 발생: ${throwable.message}", throwable)
        Toast.makeText(this, "오류: ${throwable.message}", Toast.LENGTH_SHORT).show()
    }
}