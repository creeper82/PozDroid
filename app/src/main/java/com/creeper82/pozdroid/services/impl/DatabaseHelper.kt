package com.creeper82.pozdroid.services.impl

import android.content.Context
import com.creeper82.pozdroid.services.abstraction.AppDao
import com.creeper82.pozdroid.types.Favorite
import kotlinx.coroutines.flow.Flow

object DatabaseHelper {
    private var db: AppDatabase? = null

    fun init(context: Context) {
        if (db == null) {
            db = AppDatabase.getDatabase(context)
        }
    }

    fun getDao(): AppDao {
        return db!!.appDao()
    }

    // --- FAVORITES ---

    suspend fun addFavorite(favorite: Favorite) {
        getDao().insertFavorite(favorite)
    }

    fun getFavorites(): Flow<List<Favorite>> {
        return getDao().getAllFavorites()
    }

    suspend fun deleteFavorite(favorite: Favorite) {
        getDao().deleteFavorite(favorite)
    }

    suspend fun deleteFavoriteByStopId(stopId: String) {
        getDao().deleteByStopId(stopId)
    }
}