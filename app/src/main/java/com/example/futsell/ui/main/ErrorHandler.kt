package com.example.futsell.ui.main

import android.text.TextUtils
import com.example.futsell.R
import com.example.futsell.ui.main.exception.NoInternetConnectionException
import com.example.futsell.ui.main.exception.ServerConnectionException
import com.example.futsell.ui.main.exception.UnknownInternalServerException
import com.example.futsell.ui.main.exception.UnknownServerException
import java.io.IOException

class ErrorHandler {

    fun handleError(throwable: Throwable): ErrorMessage =
        when (throwable) {
            is ServerConnectionException -> onServerConnectionError(throwable)
            is UnknownInternalServerException -> onUnknownInternalServerError(throwable)
            is UnknownServerException -> onUnknownServerError(throwable)
            is NoInternetConnectionException -> onNoInternetConnectionError(throwable)
            is IOException -> onUnknownIoException()
            is Exception -> onUnknownException()
            else -> ErrorMessage(R.string.unknown_error)
        }

    private fun onServerConnectionError(e: ServerConnectionException): ErrorMessage {
        return if (!e.message.isNullOrEmpty())
            ErrorMessage(message = e.message)
        else
            onUnknownInternalServerError(e)
    }

    private fun onUnknownServerError(e: UnknownServerException): ErrorMessage {
        return if (!e.message.isNullOrEmpty()) {
            ErrorMessage(message = e.message)
        } else {
            onUnknownInternalServerError(e)
        }
    }

    private fun onUnknownInternalServerError(e: Throwable): ErrorMessage =
        if (!e.message.isNullOrEmpty()) {
            ErrorMessage(message = e.message!!)
        } else {
            ErrorMessage(R.string.error_unhandled_server_error)
        }

    private fun onNoInternetConnectionError(e: Throwable): ErrorMessage {
        return if (!TextUtils.isEmpty(e.message))
            ErrorMessage(message = e.message!!)
        else
            ErrorMessage(R.string.error_unhandled_server_error)
    }

    private fun onUnknownIoException(): ErrorMessage {
        return ErrorMessage(R.string.error_io_exception)
    }

    private fun onUnknownException(): ErrorMessage {
        return ErrorMessage(R.string.error_unhandled_error)
    }

}