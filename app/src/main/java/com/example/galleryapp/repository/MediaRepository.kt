package com.example.galleryapp.repository

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.example.galleryapp.model.FolderItem
import com.example.galleryapp.model.MediaItem
import com.example.galleryapp.model.MediaType
import com.example.galleryapp.utils.MediaCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


class MediaRepository @Inject constructor(private val context: Context) {


    fun getAllMedia(): List<MediaItem> {
        val mediaList = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.DURATION,
            MediaStore.Files.FileColumns.DATA
        )
        val selection =
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )

        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
            val typeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val date = cursor.getLong(dateColumn)
                val type = cursor.getInt(typeColumn)
                val filePath = cursor.getString(dataColumn)
                val folderPath = File(filePath).parent ?: "Unknown Folder"
                val duration =
                    if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) cursor.getLong(
                        durationColumn
                    ) else 0L

                val uri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id)
                val mediaType =
                    if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) MediaType.IMAGE
                    else MediaType.VIDEO

                mediaList.add(
                    MediaItem(
                        id,
                        uri,
                        date,
                        duration,
                        mediaType,
                        folderPath
                    )
                )
            }
        }
        return mediaList
    }

    suspend fun getAllCachedMedia(): List<MediaItem> {
        MediaCache.getCachedMedia()?.let { return it }

        return withContext(Dispatchers.IO) {
            val media = getAllMedia()
            MediaCache.setCachedMedia(media)
            media
        }
    }

    fun groupMediaByDate(mediaList: List<MediaItem>): Map<String, List<MediaItem>> {
        return mediaList.groupBy { item ->
            SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(item.date * 1000))
        }
    }

    suspend fun groupMediaByFolder(): List<FolderItem> {
        val media = getAllCachedMedia()

        val allFolderItem = FolderItem(
            folderName = "All Photos",
            folderPath = "all_photos",
            mediaItems = media
        )

        return listOf(allFolderItem) + media.groupBy { it.folderPath }.map { (folderPath, mediaList) ->
            FolderItem(
                folderName = File(folderPath).name,
                folderPath = folderPath,
                mediaItems = mediaList
            )
        }
    }
}