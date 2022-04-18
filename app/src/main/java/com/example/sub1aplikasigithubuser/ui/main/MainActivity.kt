package com.example.sub1aplikasigithubuser.ui.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sub1aplikasigithubuser.R
import com.example.sub1aplikasigithubuser.backend.entity.User
import com.example.sub1aplikasigithubuser.databinding.ActivityMainBinding
import com.example.sub1aplikasigithubuser.ui.detail.DetailActivity
import com.example.sub1aplikasigithubuser.ui.favorite.FavoriteActivity
import com.example.sub1aplikasigithubuser.ui.notif.NotifSettings
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var listData: ArrayList<User> = ArrayList()
    private lateinit var adapter: ListUserAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

        adapter = ListUserAdapter(listData)

        binding.imageBtnFavorite.setOnClickListener(this)
        binding.imageBtnSettings.setOnClickListener(this)

        recyclerViewConfig()
        showTutorial()
        searchUser()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imageBtnFavorite -> {
                val mIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(
                    mIntent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                )
            }
            R.id.imageBtnSettings -> {
                val mIntent = Intent(this, NotifSettings::class.java)
                startActivity(
                    mIntent,
                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                )
            }
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Do you want to exit? ")
        builder.setPositiveButton("Yes") { _, _ ->
            super.onBackPressed()
        }
        builder.setNegativeButton("No") { _, _ ->
        }
        builder.show()
    }

    private fun searchUser() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()) {
                    return true
                } else {
                    listData.clear()
                    getUserSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })
    }

    private fun getUserSearch(id: String) {
        showData()
        binding.searchView.clearFocus()
        binding.searchView.onActionViewCollapsed()
        showRecycleList()
        binding.progressBar.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token c3ccf8fa6ee3e80b6b090ba761aa896cd8d979b1")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$id"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                hideTutorial()
                val result = responseBody?.let { String(it) }
                try {
                    val jsonArray = result?.let { JSONObject(it) }
                    val item = jsonArray?.getJSONArray("items")
                    val count = jsonArray?.getInt("total_count")
                    if (count == 0) {
                        showNoData()
                    } else {
                        showData()
                        if (item != null) {
                            for (i in 0 until item.length()) {
                                val jsonObject = item.getJSONObject(i)
                                val username: String = jsonObject.getString("login")
                                getUserDetail(username)
                            }
                        }
                    }

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun getUserDetail(id: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token c3ccf8fa6ee3e80b6b090ba761aa896cd8d979b1")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val jsonObject = JSONObject(result)
                    val userItem = User()

                    userItem.username = jsonObject.getString("login").toString()
                    userItem.name = jsonObject.getString("name").toString()
                    userItem.avatar = jsonObject.getString("avatar_url").toString()
                    userItem.company = jsonObject.getString("company").toString()
                    userItem.location = jsonObject.getString("location").toString()
                    userItem.repository = jsonObject.getString("public_repos")
                    userItem.follower = jsonObject.getString("followers")
                    userItem.following = jsonObject.getString("following")

                    listData.add(userItem)
                    showRecycleList()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun showSelectedUser(dataUsers: User) {
        User(
            dataUsers.username,
            dataUsers.name,
            dataUsers.avatar,
            dataUsers.follower,
            dataUsers.following,
            dataUsers.company,
            dataUsers.location,
            dataUsers.repository
        )
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA, dataUsers)

        this@MainActivity.startActivity(intent)
    }

    private fun showRecycleList() {
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(listData)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(dataUsers: User) {
                showSelectedUser(dataUsers)
            }
        })
    }

    private fun recyclerViewConfig() {
        binding.rvUsers.layoutManager = LinearLayoutManager(binding.rvUsers.context)
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.addItemDecoration(
            DividerItemDecoration(
                binding.rvUsers.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun showTutorial() {
        binding.tvTest1.visibility = View.INVISIBLE
        binding.rvUsers.visibility = View.INVISIBLE
        binding.imageViewTutorial.visibility = View.VISIBLE
        binding.textViewTutorial.visibility = View.VISIBLE
    }

    private fun hideTutorial() {
        binding.tvTest1.visibility = View.VISIBLE
        binding.rvUsers.visibility = View.VISIBLE
        binding.imageViewTutorial.visibility = View.INVISIBLE
        binding.textViewTutorial.visibility = View.INVISIBLE
    }

    private fun showNoData() {
        binding.tvTest1.visibility = View.INVISIBLE
        binding.rvUsers.visibility = View.INVISIBLE
        binding.textViewError.visibility = View.VISIBLE
        binding.imageViewError.visibility = View.VISIBLE
    }

    private fun showData() {
        binding.tvTest1.visibility = View.VISIBLE
        binding.rvUsers.visibility = View.VISIBLE
        binding.textViewError.visibility = View.INVISIBLE
        binding.imageViewError.visibility = View.INVISIBLE
    }
}