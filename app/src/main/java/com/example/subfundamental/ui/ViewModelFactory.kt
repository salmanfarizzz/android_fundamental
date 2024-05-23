package com.example.subfundamental.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.subfundamental.data.GithubRepository
import com.example.subfundamental.di.Injection
import com.example.subfundamental.ui.detail.DetailUserViewModel
import com.example.subfundamental.ui.favorite.FavoriteViewModel
import com.example.subfundamental.ui.mode.ModeViewModel

class ViewModelFactory private constructor(private val githubUserRepository: GithubRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(githubUserRepository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(githubUserRepository) as T
        } else if (modelClass.isAssignableFrom(ModeViewModel::class.java)) {
            return ModeViewModel(githubUserRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(githubUserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}