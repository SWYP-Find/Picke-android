package com.picke.domain.feature.comment.usecase

data class CommentUseCases(
    val loadCommentsUseCase: LoadCommentsUseCase,
    val deleteCommentUseCase: DeleteCommentUseCase,
    val reportCommentUseCase: ReportCommentUseCase,
    val createCommentUseCase: CreateCommentUseCase,
    val toggleCommentLikeUseCase: ToggleCommentLikeUseCase,
    val updateCommentUseCase: UpdateCommentUseCase
)