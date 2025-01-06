package com.example.galleryapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.example.galleryapp.activity.MediaViewerActivity
import com.example.galleryapp.adapters.MediaAdapter
import com.example.galleryapp.adapters.MediaAdapter.Companion.VIEW_TYPE_HEADER
import com.example.galleryapp.adapters.MediaAdapter.Companion.VIEW_TYPE_MEDIA
import com.example.galleryapp.databinding.FragmentMainBinding
import com.example.galleryapp.viewmodels.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: MediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MediaAdapter(emptyList())

        binding.rvMain.layoutManager = GridLayoutManager(context, 4).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.getItemViewType(position)) {
                        VIEW_TYPE_HEADER -> 4 // Full width
                        VIEW_TYPE_MEDIA -> 1 // Grid item
                        else -> 1
                    }
                }

            }
        }
        binding.rvMain.adapter = adapter

        viewModel.mediaDisplayList.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }
        viewModel.fetchGroupMedia()

    }

}