package com.example.galleryapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.adapters.FolderAdapter
import com.example.galleryapp.databinding.FragmentFolderBinding
import com.example.galleryapp.viewmodels.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FolderFragment : Fragment() {

    private lateinit var binding: FragmentFolderBinding
    private val mediaViewModel: MediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FolderAdapter(emptyList()) { folderItem ->

        }

        binding.rvFolder.layoutManager = GridLayoutManager(context, 3)
        binding.rvFolder.adapter = adapter

        mediaViewModel.folderMediaList.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
        mediaViewModel.fetchFolderMedia()
    }

}