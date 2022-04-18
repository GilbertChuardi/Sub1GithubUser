package com.example.sub1aplikasigithubuser.ui.favorite

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sub1aplikasigithubuser.R
import com.example.sub1aplikasigithubuser.backend.entity.Favorite
import com.example.sub1aplikasigithubuser.databinding.ItemRowUserBinding
import com.example.sub1aplikasigithubuser.ui.CustomOnItemClickListener
import com.example.sub1aplikasigithubuser.ui.detail.DetailActivity

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var listFavorites = ArrayList<Favorite>()
        @SuppressLint("NotifyDataSetChanged")
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorites.clear()
            }

            this.listFavorites.addAll(listFavorite)

            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(listFavorites[position])

    override fun getItemCount(): Int = listFavorites.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)

        fun bind(favorite: Favorite) {
            with(itemView) {

                binding.tvItemUsername.text = favorite.username
                binding.tvItemWork.text = favorite.company
                Glide.with(context)
                    .load(favorite.avatar)
                    .apply(RequestOptions().override(250, 250))
                    .into(binding.imgItemAvatar)

                binding.cvItem.setOnClickListener(CustomOnItemClickListener(
                    adapterPosition,
                    object : CustomOnItemClickListener.OnItemClickCallback {
                        override fun onItemClicked(view: View, position: Int) {
                            val intent = Intent(activity, DetailActivity::class.java)
                            intent.putExtra(DetailActivity.EXTRA_POSITION, position)
                            intent.putExtra(DetailActivity.EXTRA_FAVORITE, favorite)
                            activity.startActivity(intent)
                        }
                    }
                )
                )
            }
        }
    }
}