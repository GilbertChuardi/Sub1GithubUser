package com.example.consumerapp2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.consumerapp2.ui.favorite.FavoriteActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
        finish()
    }
}