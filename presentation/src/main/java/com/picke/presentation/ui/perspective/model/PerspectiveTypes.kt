package com.picke.presentation.ui.perspective.model

enum class PerspectiveStatus {
    PUBLISHED,
    PENDING,
    REJECTED;

    companion object {
        fun from(value: String): PerspectiveStatus = entries.find { it.name == value } ?: PUBLISHED
    }
}

enum class PerspectiveSort(val apiValue: String) {
    POPULAR("popular"),
    LATEST("latest")
}