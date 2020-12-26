package com.kieling.itsector.ui

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kieling.itsector.R

fun bindImageFromUrl(view: ImageView, imageUrl: String) {
    Glide.with(view.context)
        .load(Uri.parse(imageUrl))
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_baseline_image)
                .error(R.drawable.ic_baseline_image)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        )
        .into(view)
}
