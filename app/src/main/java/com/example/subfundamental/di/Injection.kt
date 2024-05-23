package com.example.subfundamental.di

import android.content.Context
import com.example.subfundamental.data.AppExecutors
import com.example.subfundamental.data.GithubRepository
import com.example.subfundamental.data.ModePreferences
import com.example.subfundamental.data.dataStore
import com.example.subfundamental.data.database.UserDatabase
import com.example.subfundamental.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): GithubRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()
        val modePreference = ModePreferences.getInstance(context.dataStore)
        return GithubRepository.getInstance(dao, appExecutors, modePreference)
    }
}