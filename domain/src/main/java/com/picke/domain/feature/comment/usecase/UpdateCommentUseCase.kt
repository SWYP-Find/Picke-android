package com.picke.domain.feature.comment.usecase

import com.picke.domain.feature.comment.repository.CommentRepository

class UpdateCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, commentId: Long, content: String): Result<Unit> =
        commentRepository.updateComment(perspectiveId, commentId, content).map { }
}