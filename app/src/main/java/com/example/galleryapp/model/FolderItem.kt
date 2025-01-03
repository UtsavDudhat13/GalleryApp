package com.example.galleryapp.model

data class FolderItem(
    val folderName: String,
    val folderPath: String,
    val mediaItems: List<MediaItem>
)
