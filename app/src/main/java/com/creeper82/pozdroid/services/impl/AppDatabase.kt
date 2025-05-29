package com.creeper82.pozdroid.services.impl

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.creeper82.pozdroid.services.abstraction.AppDao
import com.creeper82.pozdroid.types.Favorite

@Database(entities = [Favorite::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "favorites_db"
                )
                    .fallbackToDestructiveMigration() // for dev purposes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}