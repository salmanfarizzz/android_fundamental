package com.example.subfundamental.ui.mode

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subfundamental.data.GithubRepository
import kotlinx.coroutines.launch

class ModeViewModel(private val repo: GithubRepository) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> {
        return repo.getThemeSetting()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            repo.saveThemeSetting(isDarkModeActive)
        }
    }
}