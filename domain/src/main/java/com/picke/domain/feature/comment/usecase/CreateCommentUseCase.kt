package com.picke.domain.feature.comment.usecase

import com.picke.domain.feature.comment.repository.CommentRepository

class CreateCommentUseCase(
    private val commentRepository: CommentRepository
) {
    suspend operator fun invoke(perspectiveId: Long, content: String): Result<Unit> =
        commentRepository.createComment(perspectiveId, content).map { }
}