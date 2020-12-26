package com.kieling.itsector.ui.books

import android.os.Bundle
import android.util.Log
import android.view.View
import com.kieling.itsector.R
import com.kieling.itsector.ui.DaggerActivity
import kotlinx.android.synthetic.main.toolbar_main.*

class BooksActivity : DaggerActivity() {
    private val logTag = "BooksActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val booksFragment = BooksFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_fragment_container, booksFragment)
                .addToBackStack(BooksFragment::class.java.name)
                .commit()
        }
    }

    override fun onBackPressed() {
        val activeFragments = supportFragmentManager.backStackEntryCount
        Log.d(logTag, "There is(are) $activeFragments active fragment(s)")
        if (activeFragments <= 1) {
            finish()
        } else {
            toolbar_main_only_favorites.visibility = View.VISIBLE
            super.onBackPressed()
        }
    }
}
