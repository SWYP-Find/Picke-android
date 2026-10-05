package com.picke.data.feature.device.repository

import com.picke.data.common.error.toReportedFailure
import com.picke.data.feature.device.model.RegisterDeviceRequest
import com.picke.data.feature.device.datasource.DeviceApi
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.device.repository.DeviceRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val deviceApi: DeviceApi
) : DeviceRepository {

    override suspend fun registerDevice(fcmToken: String): Result<Unit> {
        return try {
            val response = deviceApi.registerDevice(
                RegisterDeviceRequest(fcmToken = fcmToken, platform = "ANDROID")
            )
            if (response.statusCode == 200) {
                Result.success(Unit)
            } else {
                Result.failure(ApiErrorException(response.error?.message ?: "디바이스 등록 실패"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.toReportedFailure()
        }
    }

    override suspend fun unregisterDevice(fcmToken: String): Result<Unit> {
        return try {
            val response = deviceApi.deleteDevice(fcmToken)
            if (response.statusCode == 200) {
                Result.success(Unit)
            } else {
                Result.failure(ApiErrorException(response.error?.message ?: "디바이스 해제 실패"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.toReportedFailure()
        }
    }
}