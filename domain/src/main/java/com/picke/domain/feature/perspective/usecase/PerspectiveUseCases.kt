package com.picke.domain.feature.perspective.usecase

data class PerspectiveUseCases(
    val getMyPerspectivesUseCase: GetMyPerspectivesUseCase,
    val deletePerspectiveUseCase: DeletePerspectiveUseCase,
    val loadMainPerspectiveUseCase: LoadMainPerspectiveUseCase,
    val loadPerspectivesUseCase: LoadPerspectivesUseCase,
    val reportPerspectiveUseCase: ReportPerspectiveUseCase,
    val retryModerationUseCase: RetryModerationUseCase,
    val createPerspectiveUseCase: CreatePerspectiveUseCase,
    val togglePerspectiveLikeUseCase: TogglePerspectiveLikeUseCase,
    val updatePerspectiveUseCase: UpdatePerspectiveUseCase
)