package com.example.galleryapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.galleryapp.R
import com.example.galleryapp.databinding.ItemImageBinding
import com.example.galleryapp.databinding.ItemVideoBinding
import com.example.galleryapp.model.MediaItem
import com.example.galleryapp.model.MediaType

class MediaViewerAdapter(
    private val mediaList: List<MediaItem>,
    private val onToolbarToggle: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_IMAGE = 0
        const val VIEW_TYPE_VIDEO = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (mediaList[position].type == MediaType.IMAGE) VIEW_TYPE_IMAGE else VIEW_TYPE_VIDEO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_IMAGE) {
            val binding =
                ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ImageViewHolder(binding)
        } else {
            val binding =
                ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return VideoViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mediaList[position]
        if (holder is ImageViewHolder) {
            holder.bind(item)
        } else if (holder is VideoViewHolder) {
            holder.bind(item)
        }
        holder.itemView.setOnClickListener { onToolbarToggle() }
    }

    class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaItem) {
            Glide.with(binding.mediaImage)
                .load(item.uri)
                .placeholder(R.color.grey)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.mediaImage)
        }
    }

    class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MediaItem) {
            binding.video.setVideoURI(item.uri)
            binding.video.start()
        }
    }

}