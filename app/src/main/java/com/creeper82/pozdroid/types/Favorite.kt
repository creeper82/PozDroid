package com.creeper82.pozdroid.types

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    val name: String,
    @PrimaryKey val bollardSymbol: String
)