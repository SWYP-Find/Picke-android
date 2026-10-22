package com.picke.domain.feature.classroom.usecase

data class ClassroomUseCases(
    val getMyClassTicketUseCase: GetMyClassTicketUseCase,
    val previewClassTicketUseCase: PreviewClassTicketUseCase,
    val registerClassTicketUseCase: RegisterClassTicketUseCase,
    val getMyClassesUseCase: GetMyClassesUseCase
)