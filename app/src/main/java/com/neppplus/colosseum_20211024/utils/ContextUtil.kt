package com.neppplus.colosseum_20211024.utils

import android.content.Context

class ContextUtil {

    companion object {
        private val prefName = "colosseumPref"

        private val TOKEN = "TOKEN"

        //setter - 저장 기능. SAVE
        fun setToken(context: Context, token: String) {

//          메모장을 불러내보자.
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
//          불러낸 메모장에 token 값 기록.
            pref.edit().putString(TOKEN, token).apply()

        }


        // getter - 조회 기능. LOAD
    }
}