package com.picke.data.common.network

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.picke.data.common.error.toReportedFailure
import com.picke.data.common.model.BaseResponse
import com.picke.domain.common.exception.ApiErrorException
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

internal inline fun <T> apiCall(block: () -> Result<T>): Result<T> = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: HttpException) {
    e.toApiErrorException().toReportedFailure()
} catch (e: Exception) {
    e.toReportedFailure()
}

internal fun HttpException.toApiErrorException(): Exception {
    val error = try {
        val errorBody = response()?.errorBody()?.string()
        Gson().fromJson(errorBody, BaseResponse::class.java)?.error
    } catch (e: JsonParseException) {
        null
    } catch (e: IOException) {
        null
    }
    return if (error == null) this else ApiErrorException(error.message ?: message(), error.code)
}