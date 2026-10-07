package com.picke.data.feature.comment.repository

import android.util.Log
import com.picke.data.BuildConfig
import com.picke.data.common.error.toReportedFailure
import com.picke.data.common.model.toResult
import com.picke.data.common.model.toUnitResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.comment.datasource.CommentApi
import com.picke.data.feature.comment.model.CommentRequestDto
import com.picke.data.feature.comment.model.toDomainModel
import com.picke.domain.common.exception.ApiErrorException
import com.picke.domain.feature.comment.model.CommentCreateBoard
import com.picke.domain.feature.comment.model.CommentLikeToggleBoard
import com.picke.domain.feature.comment.model.CommentPageBoard
import com.picke.domain.feature.comment.model.CommentUpdateBoard
import com.picke.domain.feature.comment.repository.CommentRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class CommentRepositoryImpl @Inject constructor(
    private val commentApi: CommentApi
) : CommentRepository {

    companion object {
        private const val TAG = "CommentRepositoryImpl_Picke"
    }

    override suspend fun getComments(perspectiveId: Long, cursor: String?, size: Int): Result<CommentPageBoard> = apiCall {
        commentApi.getComments(perspectiveId, cursor, size)
            .toResult("댓글 목록을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun createComment(perspectiveId: Long, content: String): Result<CommentCreateBoard> = apiCall {
        commentApi.createComment(perspectiveId, CommentRequestDto(content))
            .toResult("댓글을 작성하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun deleteComment(perspectiveId: Long, commentId: Long): Result<Unit> = apiCall {
        commentApi.deleteComment(perspectiveId, commentId)
            .toUnitResult("댓글을 삭제하지 못했습니다.")
    }

    override suspend fun updateComment(perspectiveId: Long, commentId: Long, content: String): Result<CommentUpdateBoard> = apiCall {
        commentApi.updateComment(perspectiveId, commentId, CommentRequestDto(content))
            .toResult("댓글을 수정하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun likeComment(commentId: Long): Result<CommentLikeToggleBoard> = apiCall {
        commentApi.likeComment(commentId)
            .toResult("댓글 좋아요를 등록하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun unlikeComment(commentId: Long): Result<CommentLikeToggleBoard> = apiCall {
        commentApi.unlikeComment(commentId)
            .toResult("댓글 좋아요를 취소하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun reportComment(perspectiveId: Long, commentId: Long): Result<String> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 신고 시도 - perspectiveId: $perspectiveId, 신고할 commentId: $commentId")
            val response = commentApi.reportComment(perspectiveId, commentId)
            when (response.statusCode) {
                200 -> {
                    if (BuildConfig.DEBUG) Log.d(TAG, "[API_RES] 댓글 신고 성공 - 서버 응답: ${response.data}")
                    Result.success(response.data ?: "Success")
                }
                409 -> {
                    Log.w(TAG, "[API_RES] 댓글 신고 중복 (이미 신고한 댓글입니다)")
                    Result.failure(Exception("ALREADY_REPORTED"))
                }
                else -> {
                    Log.e(TAG, "[API_RES] 댓글 신고 실패 - 에러: ${response.error?.message}")
                    Result.failure(ApiErrorException(response.error?.message ?: "댓글 신고에 실패했습니다."))
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 신고 예외 발생: ${e.message}")
            if (e.message?.contains("409") == true) {
                Result.failure(Exception("ALREADY_REPORTED"))
            } else {
                e.toReportedFailure()
            }
        }
    }
}