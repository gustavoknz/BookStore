package com.kieling.itsector.ui.books

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.kieling.itsector.R
import com.kieling.itsector.repository.api.network.Resource
import com.kieling.itsector.repository.model.db.BookDb
import com.kieling.itsector.ui.detail.DetailFragment
import com.kieling.itsector.utils.extensions.load
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_book_list.*
import kotlinx.android.synthetic.main.fragment_book_list.view.*
import javax.inject.Inject

class BooksFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var booksAdapter: BooksAdapter
    private lateinit var rootView: View
    private lateinit var toolbarFavoritesView: ImageView
    private var bookList = ArrayList<BookDb>()
    private var favoritesShowing = false
    private val logTag: String = "BooksFragment"
    private val booksViewModel: BooksViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_book_list, container, false)
        load()
        setViews()
        if (favoritesShowing) {
            observeFavoriteBooks()
        } else {
            observeAllBooks()
        }
        return rootView
    }

    private fun load() {
        rootView.book_list.layoutManager = GridLayoutManager(context, 2)
        booksAdapter = BooksAdapter(bookList)
        rootView.book_list.adapter = booksAdapter

        booksAdapter.onBookClicked = { book ->
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, DetailFragment.newInstance(book.id))
                .addToBackStack(DetailFragment::class.java.name)
                .commit()
        }
        toolbarFavoritesView = requireActivity().findViewById(R.id.toolbar_main_only_favorites)
        toolbarFavoritesView.setOnClickListener {
            if (favoritesShowing) {
                changeToAllBook()
            } else {
                changeToOnlyFavorites()
            }
            favoritesShowing = !favoritesShowing
        }
    }

    private fun changeToAllBook() {
        Log.d(logTag, "Not favorite clicked")
        toolbarFavoritesView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_heart_empty
            )
        )
        observeAllBooks()
    }

    private fun changeToOnlyFavorites() {
        toolbarFavoritesView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_heart_full
            )
        )
        observeFavoriteBooks()
    }

    private fun setViews() {
        rootView.book_list.setEmptyView(rootView.book_list_empty_view)
        rootView.book_list.setProgressView(rootView.book_list_progress_bar)
    }

    private fun observeAllBooks() {
        Log.d(logTag, "Observing all books...")
        booksViewModel.getBooks().observe(viewLifecycleOwner, {
            populateList(it)
        })
    }

    private fun observeFavoriteBooks() {
        Log.d(logTag, "Only favorites now!")
        booksViewModel.getFavoriteBooks().observe(viewLifecycleOwner, {
            populateList(it)
        })
    }

    private fun populateList(resource: Resource<List<BookDb>?>) {
        when {
            resource.status.isLoading() -> {
                Log.d(logTag, "Loading books...")
                book_list.showProgressView()
            }
            resource.status.isSuccessful() -> {
                Log.d(logTag, "Success: ${resource.data?.size} books loaded")
                resource.load(book_list) { books ->
                    bookList.clear()
                    bookList.addAll(books!!)
                    booksAdapter.notifyDataSetChanged()
                }
            }
            resource.status.isError() -> {
                Log.d(logTag, "Error loading books: ${resource.errorMessage.toString()}")
                if (resource.errorMessage != null) {
                    Toast.makeText(context, resource.errorMessage.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}
