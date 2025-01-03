package com.example.galleryapp.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.galleryapp.R
import com.example.galleryapp.databinding.ActivityMainBinding
import com.example.galleryapp.fragment.FolderFragment
import com.example.galleryapp.fragment.MainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainFragment = MainFragment()
    private val foldersFragment = FolderFragment()
    private var activeFragment: Fragment = mainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = resources.getColor(android.R.color.transparent)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        loadFragment(mainFragment)

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(binding.fragmentContainer, mainFragment, "MainFragment")
        ft.add(binding.fragmentContainer, foldersFragment, "FoldersFragment").hide(foldersFragment)
        ft.commit()

        selectedView(binding.imgImages, binding.tvImages)

        binding.llImages.setOnClickListener {
            selectedView(binding.imgImages, binding.tvImages)
            unselectedView(binding.imgFolders, binding.tvFolders)
            loadFragment(mainFragment)
        }

        binding.llFolders.setOnClickListener {
            selectedView(binding.imgFolders, binding.tvFolders)
            unselectedView(binding.imgImages, binding.tvImages)
            loadFragment(foldersFragment)
        }

    }

    private fun loadFragment(fragment: Fragment) {
        if (fragment == activeFragment) return

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.hide(activeFragment)
        ft.show(fragment)
        ft.commit()
        activeFragment = fragment

    }

    private fun selectedView(imageView: ImageView, textView: TextView) {
        imageView.setColorFilter(resources.getColor(R.color.blue))
        textView.setTextColor(resources.getColor(R.color.blue))
    }

    private fun unselectedView(imageView: ImageView, textView: TextView) {
        imageView.setColorFilter(resources.getColor(R.color.black))
        textView.setTextColor(resources.getColor(R.color.black))
    }

}