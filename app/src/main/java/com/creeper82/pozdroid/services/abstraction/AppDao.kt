package com.creeper82.pozdroid.services.abstraction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.creeper82.pozdroid.types.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Favorites
    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE bollardSymbol = :bollardSymbol")
    suspend fun deleteByStopId(bollardSymbol: String)
}