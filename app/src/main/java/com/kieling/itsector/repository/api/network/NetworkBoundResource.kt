package com.kieling.itsector.repository.api.network

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class NetworkResource<RequestType> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<RequestType>>()

    init {
        // Send loading state to UI
        result.value = Resource.loading()
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall(0)
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            response?.apply {
                when {
                    status.isSuccessful() -> setValue(this)
                    else -> setValue(Resource.error(errorMessage))
                }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<RequestType>> = result

    @MainThread
    private fun setValue(newValue: Resource<RequestType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    @MainThread
    protected abstract fun createCall(pageNumber: Int): LiveData<Resource<RequestType>>
}
