package com.kieling.itsector.repository.model.repository

import com.google.gson.annotations.SerializedName

data class SaleInfo(
    @SerializedName("buyLink") var buyLink: String? = null
)
