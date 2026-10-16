package com.picke.presentation.ui.classroom.classjoin.model

internal sealed interface ClassJoinMutation {
    data object CodeVerified : ClassJoinMutation
}