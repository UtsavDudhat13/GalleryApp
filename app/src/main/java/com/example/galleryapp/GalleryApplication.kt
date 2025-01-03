package com.example.galleryapp

import android.app.Application
import com.example.galleryapp.repository.MediaRepository
import com.example.galleryapp.utils.MediaCache
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class GalleryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        preloadMedia()
    }

    private fun preloadMedia() {
        CoroutineScope(Dispatchers.IO).launch {
            val media = MediaRepository(applicationContext).getAllMedia()
            MediaCache.setCachedMedia(media)
        }

    }
}