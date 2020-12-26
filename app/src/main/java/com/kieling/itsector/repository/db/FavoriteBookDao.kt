package com.kieling.itsector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kieling.itsector.repository.model.db.BookDb
import com.kieling.itsector.repository.model.db.FavoriteBookDb

@Dao
interface FavoriteBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteBook(favoriteBook: FavoriteBookDb)

    @Query("SELECT books_table.* FROM books_table JOIN favorite_book_table ON books_table.id = favorite_book_table.id")
    fun getFavoriteBooks(): LiveData<List<BookDb>?>

    @Query("SELECT * FROM favorite_book_table WHERE id=:bookId")
    fun getFavoriteBookById(bookId: String): LiveData<FavoriteBookDb>

    @Query("DELETE FROM favorite_book_table WHERE id=:bookId")
    fun deleteFavoriteBook(bookId: String)
}
