package com.example.subfundamental.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.subfundamental.data.database.UserDao

class GithubRepository private constructor(
    private val githubUserDao: UserDao,
    private val appExecutors: AppExecutors,
    private val pref: ModePreferences
) {

    fun setFavoriteUser(favoriteUser: FavEntity) {
        appExecutors.diskIO.execute { githubUserDao.insertFavoriteUser(favoriteUser) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavEntity> {
        return githubUserDao.getFavoriteUserByUsername(username)
    }

    fun getListFavoriteUser(): LiveData<List<FavEntity>> {
        return githubUserDao.getListFavoriteUser()
    }

    fun deleteFavoriteUser(favoriteUser: FavEntity) {
        appExecutors.diskIO.execute {
            githubUserDao.deleteFavoriteUser(favoriteUser)
        }
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        pref.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        @Volatile
        private var instance: GithubRepository? = null
        fun getInstance(
            newsDao: UserDao,
            appExecutors: AppExecutors,
            pref: ModePreferences
        ): GithubRepository =
            instance ?: synchronized(this) {
                instance ?: GithubRepository(newsDao, appExecutors, pref)
            }.also { instance = it }
    }
}