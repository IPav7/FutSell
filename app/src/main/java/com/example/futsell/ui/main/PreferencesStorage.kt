package com.example.futsell.ui.main

import android.content.Context
import androidx.core.content.edit

class PreferencesStorage() {

    private val preferences = App.appContext!!.getSharedPreferences(PREFERENCES_APP, Context.MODE_PRIVATE)

    fun setPartnerId(partnerId: String) {
        preferences.edit {
            putString(PARTNER_ID, partnerId)
        }
    }

    fun getPartnerId(): String {
        return preferences.getString(PARTNER_ID, "60539") ?: "60539"
    }

    fun setSecretKey(secretKey: String) {
        preferences.edit {
            putString(SECRET_KEY, secretKey)
        }
    }

    fun getSecretKey(): String {
        return preferences.getString(SECRET_KEY, "c6a6762b54172fe5abb6515697e057b9")
            ?: "c6a6762b54172fe5abb6515697e057b9"
    }

    companion object {
        const val PREFERENCES_APP = "FUTSELL_PREFERENCES"
        private const val PARTNER_ID = "$PREFERENCES_APP.PARTNER_ID"
        private const val SECRET_KEY = "$PREFERENCES_APP.SECRET_KEY"
    }

}