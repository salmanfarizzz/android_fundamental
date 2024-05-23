package com.example.subfundamental.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.subfundamental.data.FavEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavoriteUser(favoriteUser: FavEntity)

    @Query("SELECT * FROM fav_account WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavEntity>

    @Query("SELECT * FROM fav_account ORDER BY username ASC")
    fun getListFavoriteUser(): LiveData<List<FavEntity>>

    @Delete
    fun deleteFavoriteUser(favoriteUser: FavEntity)
}