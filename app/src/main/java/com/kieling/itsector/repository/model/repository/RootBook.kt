package com.kieling.itsector.repository.model.repository

import com.google.gson.annotations.SerializedName

data class RootBook(
    @SerializedName("items") var items: List<BookItem> = emptyList()
)
