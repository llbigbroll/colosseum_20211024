package com.neppplus.colosseum_20211024

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.neppplus.colosseum_20211024.databinding.ActivityLoginBinding
import com.neppplus.colosseum_20211024.utils.ContextUtil
import com.neppplus.colosseum_20211024.utils.ServerUtil
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupEvents()
        setValues()

    }


    override fun setupEvents() {
        binding.autoLoginCheckBox.setOnCheckedChangeListener { compoundButton, isChecked ->


//          isChecked : 클릭되어서 변경된 최종 상태값을 알려준다.
//            Toast.makeText(mContext, isChecked.toString(), Toast.LENGTH_SHORT).show()

//          ContextUtil에 변경된 값 저장
            ContextUtil.setAutoLogin(mContext, isChecked)
        }

        binding.signUpBtn.setOnClickListener {

            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }


        binding.loginBtn.setOnClickListener {

//          입력한 이메일/비번을 데이터 바인딩으로 가져오기
            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.passwordEdt.text.toString()

//          가져온 이메일/비번을 로그로 출력
            Log.d("입력이메일", inputEmail)
            Log.d("입력비번", inputPw)

//          서버의 로그인 기능에 전달
            ServerUtil.postRequestLogin(inputEmail, inputPw, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObject: JSONObject) {
//                  화면단에서 jsonObj 분석 -> 상황에 맞는 UI 처리
                    val code = jsonObject.getInt("code")

//                  로그인 성공시 -> 성공 토스트
//                  실패시 -> 왜 실패했는지 서버가 알려주는대로 토스트
                    if (code == 200) {

//                      Toast는 백그라운드에서 실행이되나 백그라운드에서 UI에 접근하면 에러가 남으로 runOnUiThread를 통해 해결
//                        runOnUiThread {
//                            Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show()
//                        }

//                      응용문제. 로그인 한 사람의 닉네임 추출 -> "~~님, 환영합니다!" 토스트로 출력
                        val dataObj = jsonObject.getJSONObject("data")
                        val userObj = dataObj.getJSONObject("user")
                        val nickName = userObj.getString("nick_name")

//                      내 정보를 인증하는 데이터 : 토큰 추출
                        val token = dataObj.getString("token")

//                      SharedPreferences 활용하여 저장해두자. => 필요할때 꺼내쓰도록.
                        ContextUtil.setToken(mContext, token)

                        runOnUiThread {
                            Toast.makeText(mContext, "${nickName}님, 환영합니다!", Toast.LENGTH_SHORT).show()

                            val myIntent = Intent(mContext, MainActivity::class.java)
                            startActivity(myIntent)
                            finish()
                        }

                    } else {
//                      서버가 알려주는 로그인 실패사유 파싱 -> 토스트
                        val message = jsonObject.getString("message")

                        runOnUiThread {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

    override fun setValues() {
        binding.autoLoginCheckBox.isChecked = ContextUtil.getAutoLogin(mContext)
    }


}