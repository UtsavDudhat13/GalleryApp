package com.example.galleryapp.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.text.format.Formatter.formatFileSize

object Utils {

    @SuppressLint("DefaultLocale")
    fun formatDuration(durationMillis: Long): String {
        val totalSeconds = durationMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    @SuppressLint("Recycle")
    fun getMediaSize(context: Context, contentResolver: ContentResolver, uri: Uri): String {
        val fileSizeBytes = contentResolver.openFileDescriptor(uri, "r")?.statSize ?: 0
        return formatFileSize(context,fileSizeBytes)
    }
}