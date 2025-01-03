package com.example.galleryapp.utils

import com.example.galleryapp.model.MediaItem

object MediaCache {

    private var allMedia: List<MediaItem>? = null

    fun getCachedMedia(): List<MediaItem>? {
        return allMedia
    }

    fun setCachedMedia(mediaItems: List<MediaItem>) {
        allMedia = mediaItems
    }

}