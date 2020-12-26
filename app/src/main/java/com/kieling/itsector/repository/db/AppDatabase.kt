package com.kieling.itsector.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kieling.itsector.repository.model.db.BookDb
import com.kieling.itsector.repository.model.db.FavoriteBookDb

@Database(entities = [BookDb::class, FavoriteBookDb::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDao
    abstract fun favoriteBookDao(): FavoriteBookDao
}
