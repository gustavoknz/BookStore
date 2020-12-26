package com.kieling.itsector.repository.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_book_table")
data class FavoriteBookDb(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String
)
