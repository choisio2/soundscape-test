package com.mobile.soundscapetest.PrefFragment

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mobile.soundscapetest.R
import com.mobile.soundscapetest.databinding.FragmentMynameBinding

class mynameFragment : Fragment() {

    private var _binding: FragmentMynameBinding? = null
    private val binding get() = _binding!!

    // 색상 정의 (파랑: 성공 / 빨강: 실패 / 회색: 기본)
    private val colorSuccess = Color.parseColor("#4511FF") // Royal Blue
    private val colorError = Color.parseColor("#F6443A")   // Red
    private val colorDefault = Color.parseColor("#4A494C") // Gray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMynameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 텍스트 입력 감지 리스너 설정
        binding.getNameInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()

                // --- 유효성 검사 로직 시작 ---

                // 조건 1. 특수문자 검사 (한글, 영문, 숫자만 허용)
                // 정규식: ^[0-9a-zA-Z가-힣]*$ -> 특수문자가 없으면 true
                val isCharValid = input.matches(Regex("^[0-9a-zA-Z가-힣]*$")) && input.isNotEmpty()

                // 조건 2. 길이 검사
                // 한글이 하나라도 포함되면 1~10자, 아니면(영문만) 2~20자
                val hasKorean = input.matches(Regex(".*[가-힣]+.*"))
                val len = input.length
                val isLengthValid = if (input.isEmpty()) {
                    false // 비어있으면 길이 조건 실패
                } else if (hasKorean) {
                    len in 1..10
                } else {
                    len in 2..20
                }

                // --- UI 업데이트 (색상 변경) ---

                // 1) 특수문자 텍스트뷰 업데이트
                updateConditionUI(binding.nameRuleCharacter, isCharValid, input.isEmpty())

                // 2) 길이 텍스트뷰 업데이트
                updateConditionUI(binding.nameRuleLength, isLengthValid, input.isEmpty())


                // --- 버튼 및 타원 이미지 표시 여부 ---
                // 두 조건이 모두 '참'일 때만 버튼을 보여줌
                if (isCharValid && isLengthValid) {
                    binding.btnNext.visibility = View.VISIBLE
                    binding.ellipse.visibility = View.VISIBLE

                    // 애니메이션
                    binding.btnNext.alpha = 0f
                    binding.btnNext.animate().alpha(1f).setDuration(300).start()
                    binding.ellipse.alpha = 0f
                    binding.ellipse.animate().alpha(1f).setDuration(300).start()

                } else {
                    binding.btnNext.visibility = View.GONE
                    binding.ellipse.visibility = View.GONE
                }
            }
        })

        // 2. 버튼 클릭 리스너
        binding.btnNext.setOnClickListener {
            // Fragment 이동 코드 (유지)
            val nextFragment = SingerFragment() // SingerFragment가 존재한다고 가정
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, nextFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    // 텍스트뷰의 색상과 아이콘 색상을 변경하는 함수
    private fun updateConditionUI(textView: TextView, isValid: Boolean, isEmpty: Boolean) {
        val color = when {
            isEmpty -> colorDefault   // 입력 없으면 회색
            isValid -> colorSuccess   // 조건 맞으면 파란색
            else -> colorError        // 조건 틀리면 빨간색
        }

        // 1. 글자 색상 변경
        textView.setTextColor(color)

        // 2. 왼쪽 체크 아이콘 색상 변경 (Tint)
        // compoundDrawables[0]은 왼쪽(start) 아이콘을 의미함
        textView.compoundDrawablesRelative[0]?.setTint(color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}