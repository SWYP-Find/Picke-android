package com.picke.domain.feature.classroom.model

data class ClassJoinPreviewBoard(
    val inviteCode: String,
    val name: String,
    val battleTitle: String,
    val battleType: String,
    val memberCount: Int,
    val capacity: Int,
    val closeAt: String
)