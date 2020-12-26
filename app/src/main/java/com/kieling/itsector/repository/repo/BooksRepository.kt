package com.kieling.itsector.repository.repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.kieling.itsector.app.AppExecutors
import com.kieling.itsector.repository.api.ApiServices
import com.kieling.itsector.repository.api.network.DbBoundResource
import com.kieling.itsector.repository.api.network.NetworkAndDbBoundResource
import com.kieling.itsector.repository.api.network.Resource
import com.kieling.itsector.repository.db.BooksDao
import com.kieling.itsector.repository.db.FavoriteBookDao
import com.kieling.itsector.repository.model.db.BookDb
import com.kieling.itsector.repository.model.db.FavoriteBookDb
import com.kieling.itsector.repository.model.repository.BookItem
import com.kieling.itsector.repository.model.repository.RootBook
import com.kieling.itsector.utils.ConnectivityUtil
import com.kieling.itsector.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BooksRepository @Inject constructor(
    private val booksDao: BooksDao,
    private val favoriteBookDao: FavoriteBookDao,
    private val apiServices: ApiServices, private val context: Context,
    private val appExecutors: AppExecutors = AppExecutors()
) {
    /**
     * Fetch the books from DB if it exist. Otherwise fetch from server and persist in the DB
     */
    fun getBooks(pageNumber: Int): LiveData<Resource<List<BookDb>?>> =
        object : NetworkAndDbBoundResource<List<BookDb>, RootBook>(appExecutors) {
            override fun saveCallResult(rootBook: RootBook) {
                if (rootBook.items.isNotEmpty()) {
                    booksDao.insertBooks(transformRepositoryToDb(rootBook.items))
                }
            }

            override fun shouldFetch(data: List<BookDb>?) = ConnectivityUtil.isConnected(context)

            //Since the API allows sorting books using only fields not available in the result
            // data (relevance or newest), it does make any sense to sort items from the DB
            override fun loadFromDb() = booksDao.getBooks()

            override fun createCall() = apiServices.getRootBook(pageNumber * Constants.PAGE_SIZE)
        }.asLiveData()

    private fun transformRepositoryToDb(items: List<BookItem>): List<BookDb> {
        val result = mutableListOf<BookDb>()
        items.forEach {
            result += BookDb(
                it.id,
                it.volumeInfo?.title,
                it.volumeInfo?.subtitle,
                it.volumeInfo?.authors,
                it.volumeInfo?.description,
                it.volumeInfo?.imageLinks?.thumbnail,
                it.saleInfo?.buyLink
            )
        }
        return result
    }

    fun getBookById(bookId: String): LiveData<Resource<BookDb>> =
        object : DbBoundResource<BookDb>() {
            override fun loadFromDb(): LiveData<BookDb> = booksDao.getBookById(bookId)
        }.asLiveData()

    fun getFavoriteBooks(pageNumber: Int): LiveData<Resource<List<BookDb>?>> =
        object : DbBoundResource<List<BookDb>?>() {
            override fun loadFromDb(): LiveData<List<BookDb>?> =
                favoriteBookDao.getFavoriteBooks(
                    pageNumber * Constants.PAGE_SIZE,
                    Constants.PAGE_SIZE
                )
        }.asLiveData()

    fun getFavoriteBookById(bookId: String): LiveData<Resource<FavoriteBookDb>> =
        object : DbBoundResource<FavoriteBookDb>() {
            override fun loadFromDb(): LiveData<FavoriteBookDb> =
                favoriteBookDao.getFavoriteBookById(bookId)
        }.asLiveData()

    fun addFavoriteBook(favoriteBookDb: FavoriteBookDb) =
        favoriteBookDao.insertFavoriteBook(favoriteBookDb)

    fun removeFavoriteBook(bookId: String) = favoriteBookDao.deleteFavoriteBook(bookId)
}
