package com.example.galleryapp.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.galleryapp.R
import com.example.galleryapp.activity.MediaViewerActivity
import com.example.galleryapp.databinding.ItemDateHeaderBinding
import com.example.galleryapp.databinding.ItemThumbnailBinding
import com.example.galleryapp.model.MediaItem
import com.example.galleryapp.model.MediaType
import com.example.galleryapp.utils.Utils


class MediaAdapter(
    var mediaList: List<MediaDisplayItem>,
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
            item.mediaItem?.let { holder.bind(it, mediaList, position) }
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
        fun bind(mediaItem: MediaItem?, mediaList: List<MediaDisplayItem>, position: Int) {
            if (mediaItem == null) return

            binding.apply {
                Glide.with(imageView)
                    .load(mediaItem.uri)
                    .thumbnail()
                    .encodeQuality(60)
                    .placeholder(R.color.grey)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)

                when (mediaItem.type) {
                    MediaType.IMAGE -> {
                        tvVideoDuration.visibility = View.GONE
                    }
                    MediaType.VIDEO -> {
                        tvVideoDuration.visibility = View.VISIBLE
                        tvVideoDuration.text = Utils.formatDuration(mediaItem.duration)
                    }
                }
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, MediaViewerActivity::class.java)
                val mediaItems = mediaList.filter { !it.isHeader }.map { it.mediaItem!! }
                intent.putParcelableArrayListExtra("media_list", ArrayList(mediaItems))
                intent.putExtra(
                    "initial_position",
                    position - mediaList.subList(0, position).count { it.isHeader })
                itemView.context.startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<MediaDisplayItem>) {
        mediaList = newList
        notifyDataSetChanged()
    }

}