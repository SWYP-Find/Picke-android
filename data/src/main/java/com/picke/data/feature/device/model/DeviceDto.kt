package com.picke.data.feature.device.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequest(
    val fcmToken: String,
    val platform: String
)