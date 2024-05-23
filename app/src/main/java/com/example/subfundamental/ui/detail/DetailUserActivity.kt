package com.example.subfundamental.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.subfundamental.R
import com.example.subfundamental.data.FavEntity
import com.example.subfundamental.data.response.UserDetailResponse
import com.example.subfundamental.databinding.ActivityDetailUserBinding
import com.example.subfundamental.ui.ViewModelFactory
import com.example.subfundamental.ui.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
   private lateinit var binding: ActivityDetailUserBinding

    private var isFavorite = false
    private var favoriteUser: FavEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val detailViewModel: DetailUserViewModel by viewModels {
            factory
        }
        setSupportActionBar(binding.detusToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.detusTablayout.layouttabViewpager2
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.detusTablayout.layouttabTab
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        val username = intent.getStringExtra(EXTRA_LOGIN)
        val avatarURl = intent.getStringExtra(EXTRA_AVATAR_URL)
        val type = intent.getStringExtra(EXTRA_TYPE)
        val url = intent.getStringExtra(EXTRA_URL)

        val btnFavorite = binding.detusBtnfavorite


        detailViewModel.detailUser.observe(this) { detailUser ->
            setDetailUserData(detailUser)
        }


        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (username != null) {
            detailViewModel.findDetailUser(username)
        }

        detailViewModel.getFavoriteUserByUsername(username.toString()).observe(this) {
            favoriteUser = it
            isFavorite = if (favoriteUser != null) {
                btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        btnFavorite.context,
                        R.drawable.favorite_red
                    )
                )
                true
            } else {
                btnFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        btnFavorite.context,
                        R.drawable.border_favorite_red
                    )
                )
                false

            }
        }

        binding.detusBtnfavorite.setOnClickListener {
            if (isFavorite) {
                detailViewModel.deleteFavoriteUser(favoriteUser as FavEntity)
            } else {
                detailViewModel.setFavoriteUser(
                    FavEntity(
                        username.toString(),
                        avatarURl.toString(),
                        type.toString(),
                        url.toString()
                    )
                )

            }
        }

        binding.detusToolbar.setOnMenuItemClickListener {menuItem ->
            when (menuItem.itemId) {
                R.id.share_bar -> {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        val content = "${username}, \n${url}"
                        putExtra(Intent.EXTRA_TEXT, content)
                        this.type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                    true
                }
                else -> false
            }
        }

    }

    private fun setDetailUserData(detailUser: UserDetailResponse) {
        binding.detusName.text = detailUser.name
        binding.detusUsername.text = detailUser.login
        binding.detusNumfollowing.text = detailUser.following.toString()
        binding.detusNumfollowers.text = detailUser.followers.toString()
        binding.detusNumrepos.text = detailUser.publicRepos.toString()
        binding.detusToolbar.title = detailUser.name

        Glide.with(this)
            .load(detailUser.avatarUrl)
            .into(binding.detusProfile)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detusProgressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

   companion object{
       const val EXTRA_LOGIN = "extra_login"
       const val EXTRA_AVATAR_URL = "extra_avatar_url"
       const val EXTRA_TYPE = "extra_type"
       const val EXTRA_URL = "extra_url"

       @StringRes
       private val TAB_TITLES = intArrayOf(
           R.string.tab_text_1,
           R.string.tab_text_2
       )
   }
}