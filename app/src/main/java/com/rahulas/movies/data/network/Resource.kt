package com.rahulas.movies.data.network

import com.rahulas.movies.data.network.Status.*

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
class Resource<T> private constructor(status: Status, data: T?, message: String?) {
    val status: Status
    val data: T?
    val message: String?

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource<T>(SUCCESS, data, null)
        }

        fun <T> error(
            msg: String?,
            data: T
        ): Resource<T> {
            return Resource<T>(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource<T>(
                LOADING,
                data,
                null
            )
        }
    }

    init {
        this.status = status
        this.data = data
        this.message = message
    }
}