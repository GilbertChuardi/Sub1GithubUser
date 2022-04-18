package com.example.consumerapp2.ui.tabs.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp2.backend.entity.Favorite
import com.example.consumerapp2.backend.entity.User
import com.example.consumerapp2.databinding.FragmentFollowersBinding
import com.example.consumerapp2.ui.detail.DetailActivity.Companion.EXTRA_FAVORITE
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowersFragment : Fragment() {


    private var listUser: ArrayList<User> = ArrayList()
    private lateinit var adapter: FollowerAdapter
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private var favorites: Favorite? = null
    private lateinit var dataFavorite: Favorite
    private lateinit var dataUser: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowerAdapter(listUser)
        listUser.clear()

        favorites = activity!!.intent.getParcelableExtra(EXTRA_FAVORITE)
        if (favorites != null) {
            dataFavorite = (activity!!.intent.getParcelableExtra(EXTRA_FAVORITE) as Favorite?)!!
            getUserFollowers(dataFavorite.username)
        } else {
            dataUser = (activity!!.intent.getParcelableExtra(EXTRA_DATA) as User?)!!
            getUserFollowers(dataUser.username)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getUserFollowers(id: String) {
        binding.progressBarFollowers.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token c3ccf8fa6ee3e80b6b090ba761aa896cd8d979b1")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id/followers"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBarFollowers.visibility = View.INVISIBLE
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)

                    if(jsonArray.length()==0){
                        binding.textViewNoFollower.visibility = View.VISIBLE
                        binding.rvFollowers.visibility = View.INVISIBLE
                    }
                    else {
                        binding.textViewNoFollower.visibility = View.INVISIBLE
                        binding.rvFollowers.visibility = View.VISIBLE
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val userItemFollower = User()

                            userItemFollower.username = jsonObject.getString("login").toString()
                            userItemFollower.avatar = jsonObject.getString("avatar_url").toString()
                            listUser.add(userItemFollower)

                            showRecyclerView()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                binding.progressBarFollowers.visibility = View.VISIBLE
            }
        })
    }

    private fun showRecyclerView() {
        binding.rvFollowers.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowers.adapter = adapter
    }

    companion object {
        var EXTRA_DATA = "0"
    }
}