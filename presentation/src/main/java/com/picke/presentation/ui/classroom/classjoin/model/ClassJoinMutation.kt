package com.picke.presentation.ui.classroom.classjoin.model

internal sealed interface ClassJoinMutation {

    sealed interface RequestFinished : ClassJoinMutation

    data object RequestFailed : RequestFinished

    data object CodeVerified : RequestFinished

    data class JoinFailed(val dialog: ClassJoinDialog) : RequestFinished

    data object NameDuplicated : RequestFinished
}