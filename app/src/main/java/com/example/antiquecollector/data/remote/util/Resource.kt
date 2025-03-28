package com.example.antiquecollector.data.remote.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/**
 * A generic class to handle API responses and convert them to Resource.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception, val message: String? = exception.localizedMessage) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

/**
 * A helper function to safely make API calls and handle errors.
 */
suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
    return withContext(Dispatchers.IO) {
        try {
            Resource.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Resource.Error(
                    throwable,
                    "Network error occurred. Please check your internet connection."
                )
                is HttpException -> {
                    val errorResponse = throwable.response()?.errorBody()?.string()
                    Resource.Error(
                        throwable,
                        errorResponse ?: "An unexpected error occurred."
                    )
                }
                else -> Resource.Error(
                    Exception(throwable),
                    "An unexpected error occurred."
                )
            }
        }
    }
}