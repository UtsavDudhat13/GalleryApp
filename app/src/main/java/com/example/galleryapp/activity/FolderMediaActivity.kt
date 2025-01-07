package com.example.galleryapp.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.R
import com.example.galleryapp.adapters.FolderMediaAdapter
import com.example.galleryapp.databinding.ActivityFolderMediaBinding
import com.example.galleryapp.model.FolderItem

class FolderMediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFolderMediaBinding
    private lateinit var folderItem: FolderItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = resources.getColor(R.color.white)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        folderItem = intent.getParcelableExtra<FolderItem>("folderItem") as FolderItem

        binding.materialToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.materialToolbar.setTitle(folderItem.folderName)

        val adapter = FolderMediaAdapter(folderItem.mediaItems) { mediaItem ->

        }

        binding.rvFolderMedia.layoutManager = GridLayoutManager(this, 4)
        binding.rvFolderMedia.adapter = adapter

    }

}