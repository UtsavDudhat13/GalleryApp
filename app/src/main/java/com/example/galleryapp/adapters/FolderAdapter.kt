package com.example.galleryapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.galleryapp.R
import com.example.galleryapp.databinding.ItemFolderBinding
import com.example.galleryapp.model.FolderItem

class FolderAdapter(
    private var folderList: List<FolderItem>,
    private val onClick: (FolderItem) -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    class FolderViewHolder(val binding: ItemFolderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return folderList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folderItem = folderList[position]
        holder.binding.apply {
            tvFolderTitle.text = folderItem.folderName
            tvFolderSize.text = folderItem.mediaItems.size.toString()
            Glide
                .with(imgFolderThumbnail)
                .load(folderItem.mediaItems.firstOrNull()?.uri)
                .placeholder(R.color.grey)
                .thumbnail()
                .encodeQuality(60)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgFolderThumbnail)

            root.setOnClickListener { onClick(folderItem) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<FolderItem>) {
        folderList = newList
        notifyDataSetChanged()
    }

}