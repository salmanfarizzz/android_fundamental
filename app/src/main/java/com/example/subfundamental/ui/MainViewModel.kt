package com.example.subfundamental.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.subfundamental.BuildConfig
import com.example.subfundamental.data.GithubRepository
import com.example.subfundamental.data.response.ItemsItem
import com.example.subfundamental.data.response.UserResponse
import com.example.subfundamental.data.retrofit.ApiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(private val githubUserRepository: GithubRepository) : ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFound = MutableLiveData<Boolean>()
    val isFound: LiveData<Boolean> = _isFound

    fun getThemeSetting(): LiveData<Boolean> {
        return githubUserRepository.getThemeSetting()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun findUser(login: String) {
        _isLoading.value = true
        _isFound.value = false

        val client = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val service = retrofit.create(ApiService::class.java)

        val call = service.getListUser(login, "Bearer ${BuildConfig.Token}")

        call.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUser.value = response.body()?.items
                    _isFound.value = response.body()?.totalCount == 0
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _isFound.value = true
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                _isFound.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}