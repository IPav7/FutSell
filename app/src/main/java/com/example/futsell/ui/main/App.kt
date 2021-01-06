package com.example.futsell.ui.main

import android.app.Application

@Suppress("unused")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {

        var appContext: Application? = null

    }

}