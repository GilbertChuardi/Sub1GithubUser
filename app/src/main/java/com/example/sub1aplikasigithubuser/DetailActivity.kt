package com.example.sub1aplikasigithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.sub1aplikasigithubuser.adapter.SectionPagerAdapter
import com.example.sub1aplikasigithubuser.data.User
import com.example.sub1aplikasigithubuser.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object{
        var EXTRA_DATA = "0"
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = R.string.title_detail.toString()

        setData()
        viewPagerConfig()
    }

    private fun setData(){
        val tvName:TextView = findViewById(R.id.detail_name)
        val tvUsername:TextView = findViewById(R.id.detail_username)
        val imgAvatar: ImageView = findViewById(R.id.img_item_avatar)
        val tvCompany:TextView = findViewById(R.id.detail_company)
        val tvLocation:TextView = findViewById(R.id.detail_location)
        val tvRepository:TextView = findViewById(R.id.detail_repository)

        val dataUser = intent.getParcelableExtra(EXTRA_DATA) as User?
        if (dataUser != null) {
            tvName.text = dataUser.name
            tvUsername.text = dataUser.username
            tvCompany.text = dataUser.company
            tvLocation.text = dataUser.location
            tvRepository.text = StringBuilder(R.string.repository).append(dataUser.repository)

            Glide.with(this)
                .load(dataUser.avatar)
                .into(imgAvatar)

        }
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        supportActionBar?.elevation = 0f
    }

}