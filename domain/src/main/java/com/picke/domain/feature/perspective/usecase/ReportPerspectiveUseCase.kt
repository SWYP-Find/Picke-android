package com.picke.domain.feature.perspective.usecase

import com.picke.domain.common.exception.ApiErrorCode
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.perspective.repository.PerspectiveRepository

sealed class ReportPerspectiveResult {
    data object Reported : ReportPerspectiveResult()
    data object AlreadyReported : ReportPerspectiveResult()
}

class ReportPerspectiveUseCase(
    private val perspectiveRepository: PerspectiveRepository
) {
    suspend operator fun invoke(perspectiveId: Long): Result<ReportPerspectiveResult> {
        return perspectiveRepository.reportPerspective(perspectiveId)
            .map { ReportPerspectiveResult.Reported as ReportPerspectiveResult }
            .recoverCatching { error ->
                if ((error as? ApiErrorException)?.code == ApiErrorCode.REPORT_409) {
                    ReportPerspectiveResult.AlreadyReported
                } else {
                    throw error
                }
            }
    }
}