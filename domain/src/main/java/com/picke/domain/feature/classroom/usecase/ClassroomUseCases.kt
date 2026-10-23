package com.picke.domain.feature.classroom.usecase

data class ClassroomUseCases(
    val getMyClassTicketUseCase: GetMyClassTicketUseCase,
    val previewClassTicketUseCase: PreviewClassTicketUseCase,
    val registerClassTicketUseCase: RegisterClassTicketUseCase,
    val getMyClassesUseCase: GetMyClassesUseCase,
    val createClassUseCase: CreateClassUseCase,
    val getClassDetailUseCase: GetClassDetailUseCase,
    val getClassBattleUseCase: GetClassBattleUseCase,
    val deleteClassUseCase: DeleteClassUseCase,
    val previewClassJoinUseCase: PreviewClassJoinUseCase,
    val joinClassUseCase: JoinClassUseCase,
    val getClassMembersUseCase: GetClassMembersUseCase,
    val updateMyClassNameUseCase: UpdateMyClassNameUseCase,
    val removeClassMemberUseCase: RemoveClassMemberUseCase
)