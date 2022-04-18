package com.example.sub1aplikasigithubuser.ui.detail

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sub1aplikasigithubuser.R
import com.example.sub1aplikasigithubuser.backend.db.DatabaseContract
import com.example.sub1aplikasigithubuser.backend.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.example.sub1aplikasigithubuser.backend.db.FavoriteHelper
import com.example.sub1aplikasigithubuser.backend.entity.Favorite
import com.example.sub1aplikasigithubuser.backend.entity.User
import com.example.sub1aplikasigithubuser.databinding.ActivityDetailBinding
import com.example.sub1aplikasigithubuser.ui.tabs.SectionPagerAdapter

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private var isFavorite = false
    private lateinit var favoriteHelper: FavoriteHelper
    private var favorites: Favorite? = null
    private var imageAvatar: String = "test"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "User info"

        favoriteHelper = FavoriteHelper.getInstance(applicationContext as Context)
        favoriteHelper.open()

        cekFavorite()
        viewPagerConfig()
        binding.detailFavorite.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.detail_favorite) {
            if (isFavorite) {
                favoriteHelper.deleteById(favorites?.username.toString())
                Toast.makeText(this, getString(R.string.delete_favorite), Toast.LENGTH_SHORT).show()
                binding.detailFavorite.setImageResource(R.drawable.ic_round_nofavorite_star_border_24)
                isFavorite = false
            } else {
                val dataFavorite = "1"
                val dataAvatar = imageAvatar

                val values = ContentValues()
                values.put(
                    DatabaseContract.FavoriteColumns.USERNAME,
                    binding.detailUsername.text.toString()
                )
                values.put(
                    DatabaseContract.FavoriteColumns.NAME,
                    binding.detailName.text.toString()
                )
                values.put(DatabaseContract.FavoriteColumns.AVATAR, dataAvatar)
                values.put(
                    DatabaseContract.FavoriteColumns.COMPANY,
                    binding.detailCompany.text.toString()
                )
                values.put(
                    DatabaseContract.FavoriteColumns.LOCATION,
                    binding.detailLocation.text.toString()
                )
                values.put(
                    DatabaseContract.FavoriteColumns.REPOSITORY,
                    binding.detailRepository.text.toString()
                )
                values.put(DatabaseContract.FavoriteColumns.FAVORITE, dataFavorite)

                isFavorite = true
                contentResolver.insert(CONTENT_URI, values)
                Toast.makeText(this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show()
                binding.detailFavorite.setImageResource(R.drawable.ic_round_favorite_star_24)
            }
        }
    }

    override fun onDestroy() {
        favoriteHelper.close()
        super.onDestroy()
    }

    private fun cekFavorite() {
        favorites = intent.getParcelableExtra(EXTRA_FAVORITE)
        if (favorites != null) {
            isFavorite = true
            binding.detailFavorite.setImageResource(R.drawable.ic_round_favorite_star_24)
        }
        setData()
    }

    private fun setData() {
        val dataUser = intent.getParcelableExtra(EXTRA_DATA) as User?
        if (dataUser?.name != null) {
            binding.detailName.text = dataUser.name
            binding.detailUsername.text = dataUser.username

            if (dataUser.company != "null") {
                binding.detailCompany.text = dataUser.company
            } else binding.detailCompany.text = getString(R.string.default_working)

            if (dataUser.location != "null") {
                binding.detailLocation.text = dataUser.location
            } else binding.detailLocation.text = getString(R.string.default_location)

            binding.detailRepository.text =
                StringBuilder("Repositories : ").append(dataUser.repository)

            Glide.with(this)
                .load(dataUser.avatar)
                .into(binding.detailImgItemAvatar)
            imageAvatar = dataUser.avatar
        } else {
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
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    companion object {
        const val EXTRA_DATA = "0"
        const val EXTRA_FAV = "extra_data"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
    }
}