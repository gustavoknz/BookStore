package com.kieling.itsector.repository.api.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.kieling.itsector.app.AppExecutors

/**
 * A generic class that can provide a resource backed by both the database and the network.
 * You can read more about it in the [Architecture Guide](https://developer.android.com/arch).
 *
 * @param <ResultType>
 * @param <RequestType>
 */
abstract class NetworkAndDbBoundResource<ResultType, RequestType>
@MainThread
constructor(private val appExecutors: AppExecutors) {
    private val result = MediatorLiveData<Resource<ResultType?>>()

    init {
        // Send loading state to UI
        result.value = Resource.loading()
        val dbSource = this.loadFromDb()

        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    /**
     * Fetch the data from network, persist into the DB and then send it back to UI.
     */
    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) {
            result.setValue(Resource.loading())
        }

        result.addSource(apiResponse) { response ->
            result.removeSource(dbSource)
            result.removeSource(apiResponse)

            response?.apply {
                when {
                    status.isSuccessful() -> {
                        appExecutors.diskIO().execute {
                            processResponse(this)?.let { requestType ->
                                saveCallResult(requestType)
                            }
                            appExecutors.mainThread().execute {
                                result.addSource(loadFromDb()) { newData ->
                                    setValue(Resource.success(newData))
                                }
                            }
                        }
                    }
                    else -> {
                        result.addSource(dbSource) {
                            result.setValue(Resource.error(errorMessage))
                        }
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType?>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    fun asLiveData(): LiveData<Resource<ResultType?>> = result

    @WorkerThread
    private fun processResponse(response: Resource<RequestType>): RequestType? = response.data

    @WorkerThread
    protected abstract fun saveCallResult(rootBook: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<Resource<RequestType>>
}
