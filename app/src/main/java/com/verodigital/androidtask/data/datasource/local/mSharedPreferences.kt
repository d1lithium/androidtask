package com.verodigital.androidtask.data.datasource.local

import android.content.Context
import android.content.SharedPreferences

class mSharedPreferences {

    companion object {
        private lateinit var s: mSharedPreferences
        private var sharedPreferences: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null
        fun getSharedPreference(context: Context): SharedPreferences? {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences("VERODIGITAL-SF", Context.MODE_APPEND)
                return sharedPreferences
            }
            return sharedPreferences
        }

        fun getSharedPreferenceEditor(context: Context): SharedPreferences.Editor? {
            return if (editor == null) {
                if (sharedPreferences != null) {
                    editor = sharedPreferences!!.edit()
                    editor
                } else {
                    getSharedPreference(context)
                    getSharedPreferenceEditor(context)
                }
            } else editor
        }

        fun getInstance(context: Context?): mSharedPreferences {
            if (s == null) {
                s = mSharedPreferences()

            }
            return s
        }


    }

}