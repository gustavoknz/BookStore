package com.kieling.itsector.ui.books

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kieling.itsector.R
import com.kieling.itsector.repository.model.db.BookDb
import com.kieling.itsector.ui.bindImageFromUrl
import kotlinx.android.synthetic.main.book_item_list.view.*
import kotlinx.android.synthetic.main.fragment_detail.*

class BooksAdapter(private val bookList: List<BookDb>) :
    RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {
    private val logTag = "BooksAdapter"

    private val viewTypeItem = 0
    private val viewTypeLoading = 1

    var onBookClicked: ((BookDb) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BooksViewHolder(
            if (viewType == viewTypeItem) {
                LayoutInflater.from(parent.context).inflate(
                    R.layout.book_item_list,
                    parent,
                    false
                )
            } else {
                LayoutInflater.from(parent.context).inflate(
                    R.layout.book_item_loading,
                    parent,
                    false
                )
            }
        )

    override fun onBindViewHolder(holderBooks: BooksViewHolder, position: Int) =
        holderBooks.bindView(bookList[position])

    override fun getItemCount(): Int = bookList.size

    inner class BooksViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val tvBookTitle: TextView = view.item_title
        private val ivBookThumbnail: ImageView = view.item_thumbnail

        init {
            view.setOnClickListener {
                Log.d(logTag, "Book clicked: ${bookList[adapterPosition].title}")
                onBookClicked?.invoke(bookList[adapterPosition])
            }
        }

        fun bindView(book: BookDb) {
            tvBookTitle.text = book.title
            bindImageFromUrl(ivBookThumbnail, book.thumbnail)
        }
    }
}
