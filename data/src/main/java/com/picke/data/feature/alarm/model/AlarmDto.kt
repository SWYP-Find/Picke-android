package com.picke.data.feature.alarm.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.alarm.model.AlarmDetailBoard
import com.picke.domain.feature.alarm.model.AlarmItemBoard
import com.picke.domain.feature.alarm.model.AlarmPageBoard
import kotlinx.serialization.Serializable

@Serializable
data class AlarmItemDto(
    val notificationId: Long,
    val category: String,
    val detailCode: String,
    val title: String,
    val body: String?,
    val referenceId: Long?,
    val perspectiveId: Long?,
    val isRead: Boolean,
    val createdAt: String
)

@Serializable
data class AlarmPageDto(
    val items: List<AlarmItemDto>,
    val hasNext: Boolean
)

@Serializable
data class AlarmUnreadDto(
    val hasUnread: Boolean
)

@Serializable
data class AlarmDetailDto(
    val notificationId: Long,
    val perspectiveId: Long?,
    val category: String,
    val detailCode: String,
    val title: String,
    val body: String?,
    val referenceId: Long?,
    val isRead: Boolean,
    val createdAt: String,
    val readAt: String?
)

// DTO -> Domain Mapper
fun AlarmDetailDto.toDomainModel() = AlarmDetailBoard(
    notificationId = notificationId,
    category = category,
    detailCode = detailCode,
    title = title,
    body = body.requireField("AlarmDetailDto.body"),
    referenceId = referenceId,
    perspectiveId = perspectiveId,
    isRead = isRead,
    createdAt = createdAt.take(10),
    readAt = readAt?.take(10)
)

fun AlarmItemDto.toDomainModel() = AlarmItemBoard(
    notificationId = notificationId,
    category = category,
    detailCode = detailCode,
    title = title,
    body = body.requireField("AlarmItemDto.body"),
    referenceId = referenceId,
    perspectiveId = perspectiveId,
    isRead = isRead,
    createdAt = createdAt
)

fun AlarmPageDto.toDomainModel() = AlarmPageBoard(
    items = items.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
    hasNext = hasNext
)