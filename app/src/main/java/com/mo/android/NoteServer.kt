package com.mo.android

import android.content.Context
import android.content.SharedPreferences
import com.mo.android.assist.CONTEXT


object NoteServer {

    val context: Context
        get() = CONTEXT

    val sp: SharedPreferences = context.getSharedPreferences("jin", Context.MODE_PRIVATE)

    fun saveString(tag: String, content: String) {
        val editor = sp.edit()
        editor.putString(tag, content)
        editor.apply()
    }

    fun getString(tag: String): String {
        return sp.getString(tag, "") ?: ""
    }

    fun saveInt(tag: String, content: Int) {
        val editor = sp.edit()
        editor.putInt(tag, content)
        editor.apply()
    }

    fun getInt(tag: String): Int {
        return sp.getInt(tag, 0)
    }

    fun saveLong(tag: String, content: Long) {
        val editor = sp.edit()
        editor.putLong(tag, content)
        editor.apply()
    }

    fun getLong(tag: String): Long {
        return sp.getLong(tag, 0)
    }
}