package com.ypz.ktandroidcomposesimple

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.ypz.ktandroidcomposesimple.tools.DataStoreTools
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LearnAndroidApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
        DataStoreTools.init(this)
    }
}