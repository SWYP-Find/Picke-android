package com.picke.data.common.error

internal fun <T : Any> T?.requireField(fieldName: String): T =
    this ?: throw InvalidResponseException(fieldName)

internal inline fun <T> mapItemOrNull(block: () -> T): T? = try {
    block()
} catch (e: InvalidResponseException) {
    reportIfUnexpected(e)
    null
}