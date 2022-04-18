package com.example.consumerapp2.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.consumerapp2.R
import com.example.consumerapp2.backend.entity.Favorite
import com.example.consumerapp2.databinding.ActivityDetailBinding
import com.example.consumerapp2.ui.tabs.SectionPagerAdapter

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var imageAvatar: String = "test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "User info"

        setData()
        viewPagerConfig()
    }

    private fun setData() {
            val favoriteUser = intent.getParcelableExtra(EXTRA_FAVORITE) as Favorite?

            binding.detailName.text = favoriteUser?.name
            binding.detailUsername.text = favoriteUser?.username

            if (favoriteUser?.company != "null") {
                binding.detailCompany.text = favoriteUser?.company
            } else binding.detailLocation.text = getString(R.string.default_working)

            if (favoriteUser?.location != "null") {
                binding.detailLocation.text = favoriteUser?.location
            } else binding.detailLocation.text = getString(R.string.default_location)

            binding.detailRepository.text = favoriteUser?.repository

            Glide.with(this)
                .load(favoriteUser?.avatar)
                .into(binding.detailImgItemAvatar)
            imageAvatar = favoriteUser?.avatar.toString()

    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
    }
}