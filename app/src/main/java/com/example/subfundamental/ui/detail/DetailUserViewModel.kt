package com.example.subfundamental.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subfundamental.data.FavEntity
import com.example.subfundamental.data.GithubRepository
import com.example.subfundamental.data.response.UserDetailResponse
import com.example.subfundamental.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val githubUserRepository: GithubRepository) : ViewModel() {

    private val _detailUser = MutableLiveData<UserDetailResponse>()
    val detailUser: LiveData<UserDetailResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setFavoriteUser(favoriteUser: FavEntity) {
        githubUserRepository.setFavoriteUser(favoriteUser)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavEntity> {
        return githubUserRepository.getFavoriteUserByUsername(username)
    }

    fun deleteFavoriteUser(favoriteUser: FavEntity) =
        githubUserRepository.deleteFavoriteUser(favoriteUser)

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun findDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}