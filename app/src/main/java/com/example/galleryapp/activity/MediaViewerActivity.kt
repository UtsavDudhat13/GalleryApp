package com.example.galleryapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.galleryapp.R
import com.example.galleryapp.adapters.MediaViewerAdapter
import com.example.galleryapp.databinding.ActivityMediaViewerBinding
import com.example.galleryapp.databinding.DialogInfoBinding
import com.example.galleryapp.model.MediaItem
import com.example.galleryapp.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog

class MediaViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaViewerBinding
    private var mediaList: List<MediaItem> = emptyList()
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(R.color.black)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        mediaList = intent.getParcelableArrayListExtra("media_list") ?: emptyList()
        position = intent.getIntExtra("initial_position", 0)

        updateDetails(position)

        val adapter = MediaViewerAdapter(mediaList) { toggleToolbar() }
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(position, false)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                this@MediaViewerActivity.position = position
                updateDetails(position)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_media, menu)
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_share) {
            Toast.makeText(this,"Share",Toast.LENGTH_SHORT).show()
            return true
        } else if (id == R.id.menu_delete) {
            val dialog = BottomSheetDialog(this)
            val mBinding = DialogInfoBinding.inflate(layoutInflater)
            val mediaItem = mediaList[position]
            mBinding.tvName.text = mediaItem.name
            mBinding.tvPath.text = mediaItem.folderPath + "/" + mediaItem.name
            mBinding.tvSize.text =
                Utils.getMediaSize(this@MediaViewerActivity, contentResolver, mediaItem.uri)

            dialog.setContentView(mBinding.root)
            dialog.show()
            return true
        } else if (id == R.id.menu_delete) {
            Toast.makeText(this,"Delete",Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }


    private fun updateDetails(position: Int) {
        val mediaItem = mediaList[position]
        binding.toolbar.setTitle(mediaItem.name)

    }

    private fun toggleToolbar() {
        if (binding.toolbar.visibility == View.VISIBLE) {
            binding.toolbar.animate().translationY(-binding.toolbar.height.toFloat())
                .withEndAction {
                    binding.toolbar.visibility = View.GONE
                }
        } else {
            binding.toolbar.visibility = View.VISIBLE
            binding.toolbar.animate().translationY(0f)
        }
    }
}