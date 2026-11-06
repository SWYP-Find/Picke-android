package com.picke.data.common.network

import kotlinx.serialization.json.Json

internal val PickeJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}