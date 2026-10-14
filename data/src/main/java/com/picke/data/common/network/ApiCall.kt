package com.picke.data.common.network

import com.picke.data.common.error.toReportedFailure
import kotlin.coroutines.cancellation.CancellationException

internal inline fun <T> apiCall(block: () -> Result<T>): Result<T> = try {
    block()
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    e.toReportedFailure()
}