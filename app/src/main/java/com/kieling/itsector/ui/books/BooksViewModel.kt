package com.kieling.itsector.ui.books

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kieling.itsector.repository.api.network.Resource
import com.kieling.itsector.repository.model.db.BookDb
import com.kieling.itsector.repository.model.db.FavoriteBookDb
import com.kieling.itsector.repository.repo.BooksRepository
import javax.inject.Inject

class BooksViewModel @Inject
constructor(private val booksRepository: BooksRepository) : ViewModel() {
    private var currentPage = 0
    private val logTag = "BooksRepository"
    val isFavorite = MutableLiveData<Boolean>()

    fun incrementCurrentPage() = ++currentPage

    fun getBookById(bookId: String): LiveData<Resource<BookDb>> =
        booksRepository.getBookById(bookId)

    fun getBooks(): LiveData<Resource<List<BookDb>?>> {
        Log.d(logTag, "Searching for books in page $currentPage")
        return booksRepository.getBooks(currentPage)
    }

    fun initFavorite(bookId: String) = booksRepository.getFavoriteBookById(bookId)

    fun getFavoriteBooks(): LiveData<Resource<List<BookDb>?>> =
        booksRepository.getFavoriteBooks(currentPage)

    fun favoriteBookClicked(bookId: String) {
        if (isFavorite.value == true) {
            Log.d(logTag, "Now book '$bookId' is NOT favorite")
            removeFavorite(bookId)
            isFavorite.postValue(false)
        } else {
            Log.d(logTag, "Now book '$bookId' IS favorite")
            addFavorite(FavoriteBookDb(bookId))
            isFavorite.postValue(true)
        }
    }

    private fun addFavorite(favoriteBookDb: FavoriteBookDb) {
        booksRepository.addFavoriteBook(favoriteBookDb)
        isFavorite.postValue(true)
    }

    private fun removeFavorite(bookId: String) {
        booksRepository.removeFavoriteBook(bookId)
        isFavorite.postValue(false)
    }

    fun resetCurrentPage() {
        currentPage = 0
    }
}
