package com.example.galleryapp.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
    val id: Long,
    val uri: Uri,
    val date: Long,
    val duration: Long = 0L,
    val type: MediaType,
    val folderPath: String
) : Parcelable