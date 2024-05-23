package com.example.subfundamental.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.subfundamental.data.FavEntity
import com.example.subfundamental.data.GithubRepository

class FavoriteViewModel(private val githubUserRepository: GithubRepository) : ViewModel() {
    fun getListFavoriteUser(): LiveData<List<FavEntity>> {
        return githubUserRepository.getListFavoriteUser()
    }
}