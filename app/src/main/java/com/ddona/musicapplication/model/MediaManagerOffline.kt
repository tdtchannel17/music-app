package com.ddona.musicapplication.model

import android.media.MediaPlayer

class MediaManagerOffline {
    private var mp: MediaPlayer? = null

    fun setData(path: String) {
        mp?.release()
        mp = MediaPlayer()
        mp?.setOnErrorListener(object : MediaPlayer.OnErrorListener {
            override fun onError(mp2: MediaPlayer?, what: Int, extra: Int): Boolean {
                mp = null
                return true
            }
        })
        mp?.setDataSource(path)
        mp?.prepare()
    }

    fun mp(): MediaPlayer? {
        return mp
    }

    fun mpNull(){
        mp = null
    }

    fun play() {
        mp?.start()
    }

    fun pause() {
        mp?.pause()
    }

    fun stop() {
        mp?.stop()
    }

    fun release(){
        mp?.release()
    }

    fun reset(){
        mp?.reset()
    }
}