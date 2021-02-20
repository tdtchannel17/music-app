package com.ddona.musicapplication.model

data class MusicOnline(
    val name:String,
    val artist:String,
    val linkHtml:String,
    val linkImage:String?=null,
    var linkMusic:String?=null
)