package com.ypz.ktandroidcomposesimple.common.net

import java.lang.Exception

sealed class HttpResult<out T> {

    data class Success<T>(val result: T): HttpResult<T>()
    data class Error(val exception: Exception): HttpResult<Nothing>()

}
