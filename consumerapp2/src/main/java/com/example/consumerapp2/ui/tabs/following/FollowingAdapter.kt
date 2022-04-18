package com.example.consumerapp2.ui.tabs.following

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp2.R
import com.example.consumerapp2.backend.entity.User
import com.example.consumerapp2.databinding.ItemRowUserFragmentBinding

class FollowingAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<FollowingAdapter.ListViewHolder>() {

    private var followingFilterList = listUser

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_user_fragment, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) =
        holder.bind(listUser[position])

    override fun getItemCount(): Int = followingFilterList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemRowUserFragmentBinding.bind(itemView)

        fun bind(user: User) {
            binding.tvItemUsername.text = user.username
            Glide.with(itemView.context)
                .load(user.avatar)
                .apply(RequestOptions().override(150, 150)
                    .placeholder(R.drawable.ic_round_refresh_24)
                    .error(R.drawable.ic_baseline_error_outline_24))
                .into(binding.imgItemAvatar)
        }
    }
}