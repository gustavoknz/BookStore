package com.kieling.itsector.repository.model.repository

import com.google.gson.annotations.SerializedName

data class BookItem(
    @SerializedName("id") var id: String,
    @SerializedName("selfLink") var selfLink: String? = null,
    @SerializedName("volumeInfo") var volumeInfo: VolumeInfo? = null,
    @SerializedName("saleInfo") var saleInfo: SaleInfo? = null
)
