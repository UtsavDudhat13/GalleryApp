package com.example.galleryapp

import android.app.Application
import com.example.galleryapp.repository.MediaRepository
import com.example.galleryapp.utils.MediaCache
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class GalleryApplication : Application() {

    @Inject
    lateinit var mediaRepository: MediaRepository

    override fun onCreate() {
        super.onCreate()
        preloadMedia()
    }

    private fun preloadMedia() {
        CoroutineScope(Dispatchers.IO).launch {
            val media = mediaRepository.getAllMedia()
            MediaCache.setCachedMedia(media)
        }

    }
}