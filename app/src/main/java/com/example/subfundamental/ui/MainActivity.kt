package com.example.subfundamental.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.subfundamental.R
import com.example.subfundamental.data.response.ItemsItem
import com.example.subfundamental.databinding.ActivityMainBinding
import com.example.subfundamental.ui.adapter.UserListAdapter
import com.example.subfundamental.ui.favorite.FavoriteActivity
import com.example.subfundamental.ui.mode.ModeActivity
import com.google.android.material.search.SearchView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var onBackPressedCallback: OnBackPressedCallback

    companion object {
        private var USER_LOGIN = "user_login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedCallback = this@MainActivity.onBackPressedDispatcher.addCallback(this@MainActivity, false) {
            binding.searchViewmain.hide()
        }

        val layoutManager = LinearLayoutManager(this)
        binding.itemListUser.rvListUser.layoutManager = layoutManager

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val mainViewModel: MainViewModel by viewModels {
            factory
        }

        mainViewModel.findUser("")

        mainViewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            setTheme(isDarkModeActive)
        }

        mainViewModel.listUser.observe(this) { listUser ->
            setListUser(listUser)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isFound.observe(this) {
            showDataIsFound(it)
        }

        with(binding) {
            searchViewmain.apply {
                searchViewmain.setupWithSearchBar(search)
                addTransitionListener { _, _, newState ->
                    if (newState == SearchView.TransitionState.SHOWN || newState == SearchView.TransitionState.SHOWING) {
                        searchViewmain.visibility = View.VISIBLE
                    } else {
                        searchViewmain.visibility = View.GONE
                    }
                    onBackPressedCallback.isEnabled = newState == SearchView.TransitionState.SHOWN || newState == SearchView.TransitionState.SHOWING
                }
                editText.setOnEditorActionListener { _, _, _ ->
                    search.setText(searchViewmain.text)
                    USER_LOGIN = searchViewmain.text.toString()
                    searchViewmain.hide()
                    mainViewModel.findUser(USER_LOGIN)
                    false
                }
            }
        }

        binding.Maintoolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuFavorite -> {
                    val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.menuMode -> {
                    val intent = Intent(this@MainActivity, ModeActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }


    private fun setListUser(user: List<ItemsItem>) {
        val adapter = UserListAdapter()
        adapter.submitList(user)
        binding.itemListUser.rvListUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.itemListUser.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDataIsFound(isFound: Boolean) {
        if (isFound) {
            binding.itemListUser.ilistNotFound.visibility = View.VISIBLE
        } else {
            binding.itemListUser.ilistNotFound.visibility = View.INVISIBLE
            binding.itemListUser.ilistSearch.visibility = View.INVISIBLE
        }
    }

    private fun setTheme(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}