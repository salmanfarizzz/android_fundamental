package com.example.subfundamental.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subfundamental.data.response.ItemsItem
import com.example.subfundamental.databinding.ActivityFavoriteBinding
import com.example.subfundamental.ui.ViewModelFactory
import com.example.subfundamental.ui.adapter.UserListAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.favToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavUsername.layoutManager = layoutManager

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val favoriteViewModel: FavoriteViewModel by viewModels {
            factory
        }

        favoriteViewModel.getListFavoriteUser().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(
                    login = it.username,
                    type = it.type,
                    avatarUrl = it.avatarUrl,
                    url = it.userUrl
                )
                items.add(item)
            }
            setListFavoriteUser(items)
        }
    }

    private fun setListFavoriteUser(user: List<ItemsItem>) {
        val adapter = UserListAdapter()
        adapter.submitList(user)
        binding.rvFavUsername.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}