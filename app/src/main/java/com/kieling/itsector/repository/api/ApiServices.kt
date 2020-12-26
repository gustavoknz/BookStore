package com.kieling.itsector.repository.api

import androidx.lifecycle.LiveData
import com.kieling.itsector.repository.api.network.Resource
import com.kieling.itsector.repository.model.repository.RootBook
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("volumes?q=android&maxResults=20")
    fun getRootBook(@Query ("startIndex") startIndex: Int): LiveData<Resource<RootBook>>
}
