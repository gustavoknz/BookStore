package com.kieling.itsector.repository.model.repository

import com.google.gson.annotations.SerializedName

data class ImageLinks(
    @SerializedName("thumbnail") var thumbnail: String? = null
)
