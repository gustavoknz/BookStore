package com.kieling.itsector.utils.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kieling.itsector.repository.api.network.Status

/**
 * A custom implementation of [RecyclerView] to support empty view and loading animation
 */
class CompleteRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var mEmptyView: View? = null
    private var mProgressView: View? = null
    private var columnWidth: Int = 0

    init {
        this.visibility = GONE
        if (attrs != null) {
            val attrsArray = intArrayOf(android.R.attr.columnWidth)
            val array = context.obtainStyledAttributes(attrs, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        this.visibility = VISIBLE
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(mAdapterObserver)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(mAdapterObserver)
        refreshState()
    }

    fun refreshState() {
        adapter?.let {
            mProgressView?.visibility = GONE
            if (it.itemCount == 0) {
                mEmptyView?.visibility = VISIBLE
                this.visibility = GONE
            } else {
                mEmptyView?.visibility = GONE
                this.visibility = VISIBLE
            }
        }
    }

    fun setEmptyView(emptyView: View) {
        mEmptyView = emptyView
        mEmptyView?.visibility = GONE
    }

    fun setProgressView(progressView: View) {
        mProgressView = progressView
        mProgressView?.visibility = GONE
    }

    fun showProgressView() {
        mProgressView?.visibility = VISIBLE
        mEmptyView?.visibility = GONE
    }

    fun showState(status: Status) {
        when (status) {
            Status.SUCCESS, Status.ERROR -> {
                mProgressView?.visibility = GONE
                mEmptyView?.visibility = VISIBLE
            }
            Status.LOADING -> {
                mEmptyView?.visibility = GONE
                mProgressView?.visibility = VISIBLE
            }
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (layoutManager is GridLayoutManager) {
            val manager = layoutManager as GridLayoutManager
            if (columnWidth > 0) {
                manager.spanCount = 1.coerceAtLeast(measuredWidth / columnWidth)
            }
        }
    }

    /**
     * Observe changes in the adapter
     */
    private val mAdapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() = refreshState()
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = refreshState()
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = refreshState()
    }
}
