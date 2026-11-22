package com.picke.presentation.ui.classroom.classjoin.model

enum class ClassJoinStep {
    CODE,
    NAME
}

enum class ClassCodeBoxState {
    FILLED,
    ACTIVE,
    ERROR,
    EMPTY
}

enum class ClassJoinDialog {
    INVALID_CODE,
    MEMBER_REMOVED,
    CLASS_FULL,
    CLASS_CLOSED,
    TICKET_ENDED,
    CLASS_DELETED,
    ALREADY_JOINED
}