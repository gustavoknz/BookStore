package com.kieling.itsector.repository.model.repository

import com.google.gson.annotations.SerializedName

data class RootBook(
    @SerializedName("items") var items: List<BookItem> = emptyList()
)

data class BookItem constructor(
    @SerializedName("id") var id: String,
    @SerializedName("selfLink") var selfLink: String? = null,
    @SerializedName("volumeInfo") var volumeInfo: VolumeInfo? = null,
    @SerializedName("saleInfo") var saleInfo: SaleInfo? = null
)

data class VolumeInfo(
    @SerializedName("title") var title: String? = null,
    @SerializedName("subtitle") var subtitle: String? = null,
    @SerializedName("authors") var authors: List<String> = emptyList(),
    @SerializedName("description") var description: String? = null,
    @SerializedName("imageLinks") var imageLinks: ImageLinks? = null
)

data class SaleInfo(
    @SerializedName("buyLink") var buyLink: String? = null
)

data class ImageLinks(
    @SerializedName("thumbnail") var thumbnail: String? = null
)
