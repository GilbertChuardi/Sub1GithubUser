package com.example.sub1aplikasigithubuser.ui.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sub1aplikasigithubuser.R
import com.example.sub1aplikasigithubuser.backend.entity.User
import com.example.sub1aplikasigithubuser.databinding.ItemRowUserBinding
import com.example.sub1aplikasigithubuser.ui.detail.DetailActivity
import java.util.*

class ListUserAdapter(val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(), Filterable {

    var userFilterList = listUser
    lateinit var mContext: Context

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        mContext = viewGroup.context
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSearch = constraint.toString()
                userFilterList = if (charSearch.isEmpty()) {
                    listUser
                } else {
                    val resultList = ArrayList<User>()
                    for (row in userFilterList) {
                        if ((row.username.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT)))
                        ) {
                            resultList.add(
                                User(
                                    row.username,
                                    row.name,
                                    row.avatar,
                                    row.follower,
                                    row.following,
                                    row.company,
                                    row.location,
                                    row.repository
                                )
                            )
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = userFilterList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                userFilterList = results.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = ItemRowUserBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(user: User) {
            binding.tvItemUsername.text = user.username

            binding.cvItem.animation =
                AnimationUtils.loadAnimation(itemView.context, R.anim.animasirv)

            if (user.company != "null") {
                binding.tvItemWork.text = user.company
            } else binding.tvItemWork.text = "Not Working"


            Glide.with(itemView.context)
                .load(user.avatar)
                .apply(
                    RequestOptions().override(200, 200)
                        .placeholder(R.drawable.ic_round_refresh_24)
                        .error(R.drawable.ic_baseline_error_outline_24)
                )
                .into(binding.imgItemAvatar)

            itemView.setOnClickListener {
                val dataUser = User(
                    user.username,
                    user.name,
                    user.avatar,
                    user.follower,
                    user.following,
                    user.company,
                    user.location,
                    user.repository
                )
                val intentDetail = Intent(mContext, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_DATA, dataUser)
                intentDetail.putExtra(DetailActivity.EXTRA_FAV, dataUser)
                it.context.startActivity(
                    intentDetail, ActivityOptions.makeSceneTransitionAnimation(
                        mContext as Activity?
                    ).toBundle()
                )
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: User)
    }
}