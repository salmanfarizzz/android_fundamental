package com.example.subfundamental.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.subfundamental.data.response.ItemsItem
import com.example.subfundamental.databinding.ItemUserBinding
import com.example.subfundamental.ui.detail.DetailUserActivity
import com.example.subfundamental.ui.folow.FolowFragment

class UserListAdapter : ListAdapter<ItemsItem, UserListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemsItem) {
            binding.itemName.text = item.login
            Glide.with(itemView.context)
                .load(item.avatarUrl)
                .into(binding.itemProfile)
            val context = itemView.context
            itemView.setOnClickListener {
                Intent(context, DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_LOGIN, item.login)
                    putExtra(DetailUserActivity.EXTRA_AVATAR_URL, item.avatarUrl)
                    putExtra(DetailUserActivity.EXTRA_URL, item.url)
                    putExtra(FolowFragment.ARG_USERNAME, item.login)
                }.run {
                    context.startActivity(this)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
