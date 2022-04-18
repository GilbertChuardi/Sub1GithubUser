package com.example.sub1aplikasigithubuser.ui.tabs.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sub1aplikasigithubuser.backend.entity.Favorite
import com.example.sub1aplikasigithubuser.backend.entity.User
import com.example.sub1aplikasigithubuser.databinding.FragmentFollowingBinding
import com.example.sub1aplikasigithubuser.ui.detail.DetailActivity
import com.example.sub1aplikasigithubuser.ui.tabs.follower.FollowersFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingFragment : Fragment() {
    private var listUser: ArrayList<User> = ArrayList()
    private lateinit var adapter: FollowingAdapter
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private var favorites: Favorite? = null
    private lateinit var dataFavorite: Favorite
    private lateinit var dataUser: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FollowingAdapter(listUser)
        listUser.clear()

        favorites = requireActivity().intent.getParcelableExtra(DetailActivity.EXTRA_FAVORITE)
        if (favorites != null) {
            dataFavorite =
                (requireActivity().intent.getParcelableExtra(DetailActivity.EXTRA_FAVORITE) as Favorite?)!!
            getUserFollowing(dataFavorite.username)
        } else {
            dataUser =
                (requireActivity().intent.getParcelableExtra(FollowersFragment.EXTRA_DATA) as User?)!!
            getUserFollowing(dataUser.username)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getUserFollowing(id: String) {
        binding.progressBarFollowing.visibility = View.VISIBLE

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token c3ccf8fa6ee3e80b6b090ba761aa896cd8d979b1")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$id/following"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                binding.progressBarFollowing.visibility = View.INVISIBLE

                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)

                    if (jsonArray.length() == 0) {
                        binding.textViewNoFollowing.visibility = View.VISIBLE
                        binding.rvFollowing.visibility = View.INVISIBLE
                    } else {
                        binding.textViewNoFollowing.visibility = View.INVISIBLE
                        binding.rvFollowing.visibility = View.VISIBLE
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val userItemFollowing = User()

                            userItemFollowing.username = jsonObject.getString("login").toString()
                            userItemFollowing.avatar = jsonObject.getString("avatar_url").toString()
                            listUser.add(userItemFollowing)

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
                binding.progressBarFollowing.visibility = View.VISIBLE
            }
        })
    }

    private fun showRecyclerView() {
        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter
    }
}