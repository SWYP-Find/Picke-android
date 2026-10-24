package com.picke.domain.feature.classroom.usecase

data class ClassroomUseCases(
    val getMyClassTicketUseCase: GetMyClassTicketUseCase,
    val previewClassTicketUseCase: PreviewClassTicketUseCase,
    val registerClassTicketUseCase: RegisterClassTicketUseCase,
    val getMyClassesUseCase: GetMyClassesUseCase,
    val getClassDetailUseCase: GetClassDetailUseCase,
    val getClassBattleUseCase: GetClassBattleUseCase,
    val createClassUseCase: CreateClassUseCase,
    val deleteClassUseCase: DeleteClassUseCase,
    val previewClassJoinUseCase: PreviewClassJoinUseCase,
    val joinClassUseCase: JoinClassUseCase,
    val getClassMembersUseCase: GetClassMembersUseCase,
    val updateMyDisplayNameUseCase: UpdateMyDisplayNameUseCase,
    val leaveClassUseCase: LeaveClassUseCase,
    val removeMemberUseCase: RemoveMemberUseCase
)