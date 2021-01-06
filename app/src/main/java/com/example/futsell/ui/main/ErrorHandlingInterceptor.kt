package com.example.futsell.ui.main

import com.example.futsell.ui.main.exception.ServerConnectionException
import com.example.futsell.ui.main.exception.UnknownInternalServerException
import com.example.futsell.ui.main.exception.UnknownServerException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandlingInterceptor() : IErrorHandlingInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            var request = chain.request()
            val response = chain.proceed(request)
            if (!response.isSuccessful) {
                when (response.code()) {
                    in 400..499 -> {
                        return handleClientError(response, request, chain)
                    }
                    in 500..599 -> handleServerError()
                    else -> throw getUnknownInternalException(response.toString())
                }
            }
            return response
        } catch (e: SocketTimeoutException) {
            throw ServerConnectionException("Timeout").initCause(e)
        } catch (e: UnknownHostException) {
            throw ServerConnectionException("No connection").initCause(e)
        } catch (e: ConnectException) {
            throw ServerConnectionException("No connection").initCause(e)
        }
    }


    private fun handleClientError(
        response: Response,
        request: Request,
        chain: Interceptor.Chain
    ): Response {
        try {
            throw getUnknownInternalException(response.body().toString())
        } catch (e: Exception) {
            throw getUnknownInternalException(e.message ?: response.toString()).initCause(e)
        }
    }

    @Throws(UnknownServerException::class)
    private fun handleServerError(): Nothing {
        throw UnknownServerException()
    }

    private fun getUnknownInternalException(error: String): IOException {
        return UnknownInternalServerException(error)
    }

    companion object {
        private const val BAD_CREDENTIALS_TITLE = "BadCredentialsException"
    }
}