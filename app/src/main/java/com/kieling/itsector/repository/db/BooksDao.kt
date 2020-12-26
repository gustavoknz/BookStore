package com.kieling.itsector.repository.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kieling.itsector.repository.model.db.BookDb

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(bookList: List<BookDb>)

    @Query("SELECT * FROM books_table")
    fun getBooks(): LiveData<List<BookDb>>

    @Query("SELECT * FROM books_table WHERE id=:bookId")
    fun getBookById(bookId: String): LiveData<BookDb>

    @Query("DELETE FROM books_table")
    fun deleteAllBooks()
}
