package com.kieling.itsector.ui.books

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.kieling.itsector.R
import com.kieling.itsector.repository.model.db.BookDb
import com.kieling.itsector.ui.bindImageFromUrl
import kotlinx.android.synthetic.main.book_item_list.view.*

class BooksAdapter(private val bookList: List<BookDb>) :
    RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {
    private val logTag = "BooksAdapter"

    var onBookClicked: ((BookDb) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BooksViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.book_item_list, parent, false)
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
            if (book.thumbnail == null) {
                ivBookThumbnail.setImageDrawable(
                    AppCompatResources.getDrawable(
                        view.context,
                        R.drawable.ic_baseline_image
                    )
                )
            } else {
                bindImageFromUrl(ivBookThumbnail, book.thumbnail)
            }
        }
    }
}
