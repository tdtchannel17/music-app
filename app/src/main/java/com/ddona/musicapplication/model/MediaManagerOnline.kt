package com.ddona.musicapplication.model

import android.media.MediaPlayer
import android.util.Log

class MediaManagerOnline : MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    private var mp: MediaPlayer? = null

    constructor() {

    }

    fun setPath(path: String) {
        release()
        mp = MediaPlayer()
        mp?.setOnErrorListener(this)
        mp?.setDataSource(path)
        prepareAsyn()

    }

    fun release() {
        mp?.release()
        mp = null
    }

    fun prepareAsyn() {
        mp?.setOnPreparedListener(this)
        mp?.prepareAsync()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e("MediaManagerOnline", "onError........")
        return true
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mp?.duration
        play()
    }

    fun duration() : Int {
       return mp?.duration!!.toInt()
    }

    fun mp(): MediaPlayer? {
        return mp
    }

    fun mpNull() {
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

    fun reset() {
        mp?.reset()
    }
}