package com.example.sub1aplikasigithubuser.ui.favorite

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sub1aplikasigithubuser.R
import com.example.sub1aplikasigithubuser.backend.db.DatabaseContract
import com.example.sub1aplikasigithubuser.backend.entity.Favorite
import com.example.sub1aplikasigithubuser.backend.helper.MappingHelper
import com.example.sub1aplikasigithubuser.databinding.ActivityFavoriteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Favorite User"

        binding.recycleViewFav.layoutManager = LinearLayoutManager(this)
        binding.recycleViewFav.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding.recycleViewFav.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(
            DatabaseContract.FavoriteColumns.CONTENT_URI,
            true,
            myObserver
        )

        val list = savedInstanceState?.getParcelableArrayList<Favorite>(EXTRA_STATE)
        if (list != null) {
            adapter.listFavorites = list
        }
    }

    override fun onStart() {
        super.onStart()
        loadFavoritesAsync()
    }

    override fun onRestart() {
        super.onRestart()
        loadFavoritesAsync()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorites)
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(
                    DatabaseContract.FavoriteColumns.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBarFav.visibility = View.INVISIBLE

            val favorites = deferredFavorites.await()

            if (favorites.size > 0) {
                showFavorite()
                adapter.listFavorites = favorites
                binding.activityFavorite.setBackgroundResource(R.color.background_app)
            } else {
                showNoFavorite()
                adapter.listFavorites = ArrayList()
            }
        }
    }

    private fun showFavorite() {
        binding.recycleViewFav.visibility = View.VISIBLE
        binding.imageViewNoFavorite.visibility = View.INVISIBLE
        binding.textViewNoFavorite.visibility = View.INVISIBLE
    }

    private fun showNoFavorite() {
        binding.recycleViewFav.visibility = View.INVISIBLE
        binding.imageViewNoFavorite.visibility = View.VISIBLE
        binding.textViewNoFavorite.visibility = View.VISIBLE
    }

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }
}