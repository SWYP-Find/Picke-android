package com.picke.data.common.error

internal class InvalidResponseException(fieldName: String) :
    IllegalStateException("응답에 필수 값이 없습니다: $fieldName")