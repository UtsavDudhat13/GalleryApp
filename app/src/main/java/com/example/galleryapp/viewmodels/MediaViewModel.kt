package com.example.galleryapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.adapters.MediaDisplayItem
import com.example.galleryapp.model.FolderItem
import com.example.galleryapp.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class MediaViewModel @Inject constructor(private val repository: MediaRepository) : ViewModel() {

    private val _groupMediaList = MutableLiveData<List<MediaDisplayItem>>()
    val mediaDisplayList: LiveData<List<MediaDisplayItem>> get() = _groupMediaList

    fun fetchGroupMedia() {
        viewModelScope.launch(Dispatchers.IO) {

            val allMedia = repository.getAllCachedMedia()
            val groupedMedia = withContext(Dispatchers.IO) {
                repository.groupMediaByDate(allMedia).flatMap { (date, mediaItems) ->
                    listOf(MediaDisplayItem(isHeader = true, headerText = date)) +
                            mediaItems.map { MediaDisplayItem(isHeader = false, mediaItem = it) }
                }
            }

            withContext(Dispatchers.Main) {
                _groupMediaList.postValue(groupedMedia)
            }
        }
    }


    private val _folderMediaList = MutableLiveData<List<FolderItem>>()
    val folderMediaList: LiveData<List<FolderItem>> get() = _folderMediaList

    fun fetchFolderMedia() {
        viewModelScope.launch {
            val groupedMedia = repository.groupMediaByFolder()
            _folderMediaList.postValue(groupedMedia)
        }
    }


}