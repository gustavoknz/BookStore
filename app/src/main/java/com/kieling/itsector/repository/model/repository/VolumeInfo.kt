package com.kieling.itsector.repository.model.repository

import com.google.gson.annotations.SerializedName

data class VolumeInfo(
    @SerializedName("title") var title: String? = null,
    @SerializedName("subtitle") var subtitle: String? = null,
    @SerializedName("authors") var authors: List<String> = emptyList(),
    @SerializedName("description") var description: String? = null,
    @SerializedName("imageLinks") var imageLinks: ImageLinks? = null
)
