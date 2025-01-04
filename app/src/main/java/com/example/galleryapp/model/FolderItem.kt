package com.example.galleryapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FolderItem(
    val folderName: String,
    val folderPath: String,
    val mediaItems: List<MediaItem>
) : Parcelable