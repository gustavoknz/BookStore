package com.kieling.itsector.repository.api.network

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class DbBoundResource<ResultType> @MainThread constructor() {
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        // Send loading state to UI
        result.value = Resource.loading()
        val dbSource = this.loadFromDb()

        result.addSource(dbSource) {
            result.removeSource(dbSource)
            result.addSource(dbSource) { newData ->
                setValue(Resource.success(newData))
            }
        }
    }

    fun asLiveData(): LiveData<Resource<ResultType>> = result

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>
}