package com.example.galleryapp.model

import android.net.Uri

data class MediaItem (
    val id: Long,
    val uri: Uri,
    val date: Long,
    val duration: Long = 0L,
    val type: MediaType,
    val folderPath: String
)