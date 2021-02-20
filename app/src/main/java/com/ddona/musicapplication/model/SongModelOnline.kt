package com.ddona.musicapplication.model

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SongModelOnline : ViewModel {

    val musicOnlines = MutableLiveData<MutableList<MusicOnline>>()
    val isSearchingData = ObservableBoolean(false)

    constructor(){

    }
}