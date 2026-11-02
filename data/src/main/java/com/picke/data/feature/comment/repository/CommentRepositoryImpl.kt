package com.picke.data.feature.comment.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.model.toUnitResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.comment.datasource.CommentApi
import com.picke.data.feature.comment.model.CommentRequestDto
import com.picke.data.feature.comment.model.toDomainModel
import com.picke.domain.feature.comment.model.CommentCreateBoard
import com.picke.domain.feature.comment.model.CommentLikeToggleBoard
import com.picke.domain.feature.comment.model.CommentPageBoard
import com.picke.domain.feature.comment.model.CommentUpdateBoard
import com.picke.domain.feature.comment.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentApi: CommentApi
) : CommentRepository {

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

    override suspend fun reportComment(perspectiveId: Long, commentId: Long): Result<Unit> = apiCall {
        commentApi.reportComment(perspectiveId, commentId)
            .toUnitResult("댓글을 신고하지 못했습니다.")
    }
}