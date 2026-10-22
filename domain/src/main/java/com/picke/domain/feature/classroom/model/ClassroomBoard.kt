package com.picke.domain.feature.classroom.model

data class MyClassBoard(
    val classId: Long,
    val name: String,
    val myRole: String,
    val battleId: Long,
    val battleTitle: String,
    val battleThumbnailUrl: String,
    val battleType: String,
    val memberCount: Int,
    val capacity: Int,
    val closeAt: String,
    val closed: Boolean,
    val createdAt: String
)