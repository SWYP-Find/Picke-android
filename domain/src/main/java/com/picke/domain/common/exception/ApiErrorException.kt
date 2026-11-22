package com.picke.domain.common.exception

class ApiErrorException(message: String, val code: String? = null) : Exception(message)