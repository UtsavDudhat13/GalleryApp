package com.example.galleryapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.galleryapp.R
import com.example.galleryapp.activity.MediaViewerActivity
import com.example.galleryapp.databinding.ItemThumbnailBinding
import com.example.galleryapp.model.MediaItem
import com.example.galleryapp.model.MediaType
import com.example.galleryapp.utils.Utils

class FolderMediaAdapter(
    private var mediaList: List<MediaItem>,
    private val onClick: (MediaItem) -> Unit
) : RecyclerView.Adapter<FolderMediaAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: ItemThumbnailBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemThumbnailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val mediaItem = mediaList[position]
        holder.binding.apply {
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
            root.setOnClickListener {
                val intent = Intent(holder.itemView.context, MediaViewerActivity::class.java)
                intent.putParcelableArrayListExtra("media_list", ArrayList(mediaList))
                intent.putExtra(
                    "initial_position",
                    position)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

}