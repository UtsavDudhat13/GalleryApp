package com.example.galleryapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.galleryapp.R
import com.example.galleryapp.databinding.ItemDateHeaderBinding
import com.example.galleryapp.databinding.ItemThumbnailBinding
import com.example.galleryapp.model.MediaItem
import com.example.galleryapp.model.MediaType
import com.example.galleryapp.utils.Utils


class MediaAdapter(
    private var mediaList: List<MediaDisplayItem>,
    private val onClick: (MediaItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_MEDIA = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (mediaList[position].isHeader) VIEW_TYPE_HEADER else VIEW_TYPE_MEDIA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val binding =
                ItemDateHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DateViewHolder(binding)
        } else {
            val binding =
                ItemThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MediaViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mediaList[position]
        if (holder is DateViewHolder) {
            holder.bind(item.headerText)
        } else if (holder is MediaViewHolder) {
            item.mediaItem?.let { holder.bind(it, onClick) }
        }
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    class DateViewHolder(private val binding: ItemDateHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(date: String) {
            binding.tvDate.text = date
        }
    }

    class MediaViewHolder(private val binding: ItemThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: MediaItem?, onClick: (MediaItem) -> Unit) {
            if (mediaItem == null) return

            binding.apply {
                when (mediaItem.type) {
                    MediaType.IMAGE -> {
                        Glide.with(imageView)
                            .load(mediaItem.uri)
                            .thumbnail()
                            .encodeQuality(60)
                            .placeholder(R.color.grey)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView)
                        tvVideoDuration.visibility = View.GONE
                    }

                    MediaType.VIDEO -> {
                        Glide.with(imageView)
                            .load(mediaItem.uri)
                            .thumbnail()
                            .encodeQuality(60)
                            .placeholder(R.color.grey)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageView)
                        tvVideoDuration.visibility = View.VISIBLE
                        tvVideoDuration.text = Utils.formatDuration(mediaItem.duration)
                    }

                    MediaType.UNKNOWN -> {
                        imageView.setBackgroundColor(imageView.context.getColor(R.color.grey))
                        tvVideoDuration.visibility = View.GONE
                    }
                }
            }
            itemView.setOnClickListener { onClick(mediaItem) }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<MediaDisplayItem>) {
        mediaList = newList
        notifyDataSetChanged()
    }


}