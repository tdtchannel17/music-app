package com.ddona.musicapplication.model

import android.net.Uri
import java.io.Serializable

data class MusicOffline(
    val name: String,
    val path: String,
    val duration: Long? = null,
    var uriAlbum: Uri? = null,
    val albumId: Long? = null
): Serializable