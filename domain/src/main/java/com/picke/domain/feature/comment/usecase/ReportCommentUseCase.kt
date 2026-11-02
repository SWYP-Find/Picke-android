package com.picke.domain.feature.comment.usecase

import com.picke.domain.common.exception.ApiErrorCode
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.comment.repository.CommentRepository

sealed class ReportCommentResult {
    data object Reported : ReportCommentResult()
    data object AlreadyReported : ReportCommentResult()
}

class ReportCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, commentId: Long): Result<ReportCommentResult> {
        return commentRepository.reportComment(perspectiveId, commentId)
            .map { ReportCommentResult.Reported as ReportCommentResult }
            .recoverCatching { error ->
                if ((error as? ApiErrorException)?.code == ApiErrorCode.REPORT_409) {
                    ReportCommentResult.AlreadyReported
                } else {
                    throw error
                }
            }
    }
}