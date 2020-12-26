package com.kieling.itsector.repository.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books_table")
data class BookDb(
    //Unique field for volumes. Check https://developers.google.com/books/docs/v1/reference/volumes
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "subtitle") val subtitle: String?,
    @ColumnInfo(name = "authors") val authors: List<String>?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "thumbnail") val thumbnail: String?,
    @ColumnInfo(name = "buy_link") val buyLink: String?,
)
