package com.kieling.itsector.repository.api.network

/**
 * Status of a resource that is provided to the UI
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING;

    fun isSuccessful() = this == SUCCESS
    fun isLoading() = this == LOADING
    fun isError() = this == ERROR
}
