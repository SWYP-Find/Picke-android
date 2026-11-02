package com.picke.domain.feature.perspective.usecase

data class PerspectiveUseCases(
    val loadPerspectivesUseCase: LoadPerspectivesUseCase,
    val getMyPerspectivesUseCase: GetMyPerspectivesUseCase,
    val loadMainPerspectiveUseCase: LoadMainPerspectiveUseCase,
    val createPerspectiveUseCase: CreatePerspectiveUseCase,
    val updatePerspectiveUseCase: UpdatePerspectiveUseCase,
    val deletePerspectiveUseCase: DeletePerspectiveUseCase,
    val retryModerationUseCase: RetryModerationUseCase,
    val togglePerspectiveLikeUseCase: TogglePerspectiveLikeUseCase,
    val reportPerspectiveUseCase: ReportPerspectiveUseCase
)