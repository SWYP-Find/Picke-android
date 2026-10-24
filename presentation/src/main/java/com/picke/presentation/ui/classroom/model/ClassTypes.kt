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

    companion object {
        fun from(value: String): ClassRole = entries.find { it.name == value } ?: MEMBER
    }
}

enum class ClassBattleType {
    TTS,
    CUSTOM;

    companion object {
        fun from(value: String): ClassBattleType = entries.find { it.name == value } ?: TTS
    }
}

enum class ClassTicketDialog {
    NO_PERMISSION,
    UNREGISTERED,
    EXPIRED
}