package com.example.sub1aplikasigithubuser.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sub1aplikasigithubuser.DetailActivity
import com.example.sub1aplikasigithubuser.R
import com.example.sub1aplikasigithubuser.data.User
import java.util.*
import kotlin.collections.ArrayList

var userFilterList = ArrayList<User>()
lateinit var mContext: Context

class ListUserAdapter(val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>(), Filterable {

    init {
        userFilterList = listUser
    }

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListUserAdapter.ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_user, viewGroup, false)

        val sch = ListViewHolder(view)
        mContext = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListUserAdapter.ListViewHolder, position: Int) {
        val user = listUser[position]

        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .apply(RequestOptions().override(200, 200))
            .into(holder.imgAvatar)

        holder.tvUsername.text = user.username
        holder.tvWork.text = user.company

        holder.itemView.setOnClickListener {
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
            it.context.startActivity(intentDetail)
        }
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
                        if ((row.username.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT)))
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
        var tvUsername: TextView = itemView.findViewById(R.id.tv_item_username)
        var tvWork: TextView = itemView.findViewById(R.id.tv_item_work)
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_item_avatar)
    }

    interface OnItemClickCallback {
        fun onItemClicked(dataUsers: User)
    }
}