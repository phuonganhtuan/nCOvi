package com.example.ncovi.data.datasource.remote.api

data class ResultCovi<out T>(val status: CoroutineState, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T) =
            ResultCovi(status = CoroutineState.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String) =
            ResultCovi(status = CoroutineState.ERROR, data = data, message = message)

        fun <T> loading(data: T?) =
            ResultCovi(status = CoroutineState.LOADING, data = data, message = null)
    }
}

enum class CoroutineState { SUCCESS, ERROR, LOADING }
