package com.picke.data.common.error

import com.google.gson.stream.MalformedJsonException
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.common.exception.NotEnoughPointsException
import io.sentry.Sentry
import retrofit2.HttpException
import java.io.IOException

internal fun reportIfUnexpected(e: Throwable) {
    if (e.isUnexpected()) Sentry.captureException(e)
}

internal fun <T> Exception.toReportedFailure(): Result<T> {
    reportIfUnexpected(this)
    return Result.failure(this)
}

private fun Throwable.isUnexpected(): Boolean = when (this) {
    is MalformedJsonException -> true
    is IOException -> false
    is HttpException -> false
    is ApiErrorException -> false
    is NotEnoughPointsException -> false
    else -> true
}