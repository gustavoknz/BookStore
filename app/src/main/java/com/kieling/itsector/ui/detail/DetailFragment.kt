package com.kieling.itsector.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.kieling.itsector.R
import com.kieling.itsector.repository.model.db.BookDb
import com.kieling.itsector.ui.bindImageFromUrl
import com.kieling.itsector.ui.books.BooksViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var rootView: View
    private var paramBookId: String? = null
    private var toolbarFavoritesView: ImageView? = null
    private val logTag = "DetailFragment"
    private val booksViewModel: BooksViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarFavoritesView = activity?.findViewById(R.id.toolbar_main_only_favorites)
        arguments?.let {
            paramBookId = it.getString(ARG_BOOK_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_detail, container, false)
        toolbarFavoritesView?.visibility = View.GONE
        observeBook()
        return rootView
    }

    private fun observeBook() {
        booksViewModel.getBookById(paramBookId!!).observe(viewLifecycleOwner, {
            when {
                it.status.isLoading() -> {
                    Log.d(logTag, "Loading book id=$paramBookId")
                }
                it.status.isSuccessful() -> {
                    Log.d(logTag, "Success: book title=${it.data?.title} loaded")
                    setData(it.data!!)
                }
                it.status.isError() -> {
                    Log.d(logTag, "Error loading book: ${it.errorMessage.toString()}")
                    if (it.errorMessage != null)
                        Toast.makeText(context, it.errorMessage.toString(), Toast.LENGTH_LONG)
                            .show()
                }
            }
        })
    }

    private fun setData(book: BookDb) {
        booksViewModel.isFavorite.observe(viewLifecycleOwner, { newFavorite ->
            Log.d(logTag, "newFavorite: $newFavorite")
            if (newFavorite) {
                detail_favorite.contentDescription = getString(R.string.detail_favorite_desc_yes)
                detail_favorite.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart_full)
            } else {
                detail_favorite.contentDescription = getString(R.string.detail_favorite_desc_no)
                detail_favorite.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_heart_empty)
            }
        })
        booksViewModel.initFavorite(paramBookId!!).observe(viewLifecycleOwner, {
            when {
                it.status.isLoading() -> {
                    Log.d(logTag, "Loading favorite book id=$paramBookId...")
                }
                it.status.isSuccessful() -> {
                    val bookIsFavorite = it.data != null
                    Log.d(logTag, "Success: book is favorite? $bookIsFavorite")
                    booksViewModel.isFavorite.postValue(it.data != null)
                }
                it.status.isError() -> {
                    Log.d(logTag, "Error loading favorite book: ${it.errorMessage.toString()}")
                    if (it.errorMessage != null) {
                        Toast.makeText(context, it.errorMessage.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        })
        detail_favorite.setOnClickListener {
            GlobalScope.launch { booksViewModel.favoriteBookClicked(book.id) }
        }
        detail_title.text = book.title
        detail_subtitle.text = book.subtitle
        detail_authors.text = getAuthorsToShow(book.authors)
        if (book.thumbnail == null) {
            detail_image.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_image
                )
            )
        } else {
            bindImageFromUrl(detail_image, book.thumbnail)
        }
        detail_description.text = book.description
        if (book.buyLink == null) {
            detail_link.visibility = View.GONE
        } else {
            setDetailLinkView(book.buyLink)
        }
    }

    private fun setDetailLinkView(link: Any) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            detail_link.text =
                Html.fromHtml(getString(R.string.detail_buy_link, link), Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            detail_link.text = Html.fromHtml(getString(R.string.detail_buy_link, link))
        }
        detail_link.movementMethod = LinkMovementMethod.getInstance()
        detail_link.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(detail_link.text.toString())))
        }
    }

    fun getAuthorsToShow(authors: List<String>?): CharSequence {
        val str = StringBuffer()
        authors?.forEachIndexed { index, item ->
            when {
                str.isEmpty() -> {
                    str.append(item)
                }
                authors.size - 1 > index -> {
                    str.append(", ")
                    str.append(item)
                }
                else -> {
                    str.append(" and ")
                    str.append(item)
                }
            }
        }
        return str.toString()
    }

    companion object {
        const val ARG_BOOK_ID = "plant"

        @JvmStatic
        fun newInstance(bookId: String) = DetailFragment().apply {
            arguments = Bundle().apply { putString(ARG_BOOK_ID, bookId) }
        }
    }
}
