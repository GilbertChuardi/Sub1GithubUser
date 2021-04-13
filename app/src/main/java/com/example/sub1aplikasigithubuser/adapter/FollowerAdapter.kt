package com.example.sub1aplikasigithubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sub1aplikasigithubuser.R
import com.example.sub1aplikasigithubuser.data.User
import com.example.sub1aplikasigithubuser.mContext

var followersFilterList = ArrayList<User>()

class FollowerAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<FollowerAdapter.ListViewHolder>() {
    init {
        followersFilterList = listUser
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        val sch = ListViewHolder(view)
        mContext = viewGroup.context
        return sch
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]

        Glide.with(holder.itemView.context)
                .load(user.avatar)
                .apply(RequestOptions().override(150, 150))
                .into(holder.imgAvatar)

        holder.tvUsername.text = user.username
        holder.tvWork.text = user.company
    }

    override fun getItemCount(): Int = followersFilterList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUsername: TextView = itemView.findViewById(R.id.tv_item_username)
        var tvWork: TextView = itemView.findViewById(R.id.tv_item_work)
        var imgAvatar: ImageView = itemView.findViewById(R.id.img_item_avatar)
    }


}