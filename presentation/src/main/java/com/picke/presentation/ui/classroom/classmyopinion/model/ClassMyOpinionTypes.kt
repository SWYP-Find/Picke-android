package com.picke.presentation.ui.classroom.classmyopinion.model

enum class ClassMyPerspectiveStatus {
    PUBLISHED,
    UNPUBLISHED;

    companion object {
        fun from(value: String): ClassMyPerspectiveStatus =
            entries.find { it.name == value } ?: UNPUBLISHED
    }
}