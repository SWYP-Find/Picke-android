package com.picke.presentation.ui.classroom.model

enum class ClassTicketStatus {
    NONE,
    UNREGISTERED,
    ACTIVE,
    EXPIRED;

    companion object {
        fun from(value: String): ClassTicketStatus = entries.find { it.name == value } ?: EXPIRED
    }
}

enum class ClassRole {
    OWNER,
    MEMBER;

    val isOwner: Boolean get() = this == OWNER

    companion object {
        fun from(value: String): ClassRole = entries.find { it.name == value } ?: MEMBER
    }
}

enum class ClassBattleType {
    TTS,
    CUSTOM;

    val isAiBattle: Boolean get() = this == CUSTOM

    companion object {
        fun from(value: String): ClassBattleType = entries.find { it.name == value } ?: TTS
    }
}

enum class ClassBattleStep {
    NONE,
    PRE_VOTE,
    TTS_LISTENING,
    POST_VOTE,
    COMPLETED;

    companion object {
        fun from(value: String): ClassBattleStep = entries.find { it.name == value } ?: NONE
    }
}

enum class ClassTicketDialog {
    NO_PERMISSION,
    UNREGISTERED,
    EXPIRED
}