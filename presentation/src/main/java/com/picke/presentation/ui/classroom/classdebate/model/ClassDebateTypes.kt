package com.picke.presentation.ui.classroom.classdebate.model

enum class ClassDebateSpeaker {
    AI,
    USER;

    companion object {
        fun from(value: String): ClassDebateSpeaker = entries.find { it.name == value } ?: USER
    }
}

enum class ClassDebateStatus {
    GENERATING,
    WAITING_USER,
    FAILED,
    COMPLETED;

    companion object {
        fun from(value: String): ClassDebateStatus = entries.find { it.name == value } ?: GENERATING
    }
}