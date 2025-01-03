package com.example.galleryapp.adapters

import com.example.galleryapp.model.MediaItem

data class MediaDisplayItem(
    val isHeader: Boolean,
    val headerText: String = "",
    val mediaItem: MediaItem? = null
)
