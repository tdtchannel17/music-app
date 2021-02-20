package com.ddona.musicapplication

import android.app.Application
import com.ddona.musicapplication.model.SongModelOnline

class MyApp : Application() {
    lateinit var songModelOnline :SongModelOnline
    override fun onCreate() {
        super.onCreate()
        songModelOnline = SongModelOnline()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}