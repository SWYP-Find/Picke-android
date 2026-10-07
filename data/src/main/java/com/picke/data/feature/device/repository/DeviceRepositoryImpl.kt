package com.picke.data.feature.device.repository

import com.picke.data.common.model.toUnitResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.device.datasource.DeviceApi
import com.picke.data.feature.device.model.RegisterDeviceRequest
import com.picke.domain.feature.device.repository.DeviceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val deviceApi: DeviceApi
) : DeviceRepository {

    override suspend fun registerDevice(fcmToken: String): Result<Unit> = apiCall {
        deviceApi.registerDevice(RegisterDeviceRequest(fcmToken = fcmToken, platform = "ANDROID"))
            .toUnitResult("디바이스를 등록하지 못했습니다.")
    }

    override suspend fun unregisterDevice(fcmToken: String): Result<Unit> = apiCall {
        deviceApi.deleteDevice(fcmToken)
            .toUnitResult("디바이스를 해제하지 못했습니다.")
    }
}