package com.picke.domain.feature.comment.usecase

data class CommentUseCases(
    val loadCommentsUseCase: LoadCommentsUseCase,
    val createCommentUseCase: CreateCommentUseCase,
    val updateCommentUseCase: UpdateCommentUseCase,
    val deleteCommentUseCase: DeleteCommentUseCase,
    val toggleCommentLikeUseCase: ToggleCommentLikeUseCase,
    val reportCommentUseCase: ReportCommentUseCase
)