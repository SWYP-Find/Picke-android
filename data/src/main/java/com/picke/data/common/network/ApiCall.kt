package com.picke.data.common.network

import com.picke.data.common.error.toReportedFailure
import com.picke.data.common.model.BaseResponse
import com.picke.domain.common.exception.ApiErrorException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
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
        val errorBody = response()?.errorBody()?.string() ?: return this
        PickeJson.decodeFromString<BaseResponse<JsonElement>>(errorBody).error
    } catch (e: SerializationException) {
        null
    } catch (e: IOException) {
        null
    }
    return if (error == null) this else ApiErrorException(error.message ?: message(), error.code)
}