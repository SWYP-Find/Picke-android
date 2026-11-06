package com.picke.data.feature.battle.model

import com.picke.domain.feature.battle.model.BattleDetailBoard
import com.picke.domain.feature.battle.model.BattleInfoBoard
import com.picke.domain.feature.battle.model.BattleOptionBoard
import com.picke.domain.feature.battle.model.BattleTagBoard
import kotlinx.serialization.Serializable

@Serializable
data class BattleDetailDto(
    val battleInfo: BattleInfoDto?,
    val description: String?,
    val shareUrl: String?,
    val userVoteStatus: String?,
    val currentStep: String?,
    val categoryTags: List<BattleTagDto>?,
    val philosopherTags: List<BattleTagDto>?,
    val valueTags: List<BattleTagDto>?
)

@Serializable
data class BattleInfoDto(
    val battleId: Long?,
    val title: String?,
    val summary: String?,
    val thumbnailUrl: String?,
    val viewCount: Int?,
    val participantsCount: Int?,
    val audioDuration: Int?,
    val tags: List<BattleTagDto>?,
    val options: List<BattleOptionDto>?
)

@Serializable
data class BattleOptionDto(
    val optionId: Long?,
    val title: String?,
    val stance: String?,
    val representative: String?,
    val imageUrl: String?,
    val tags: List<BattleTagDto>?
)

@Serializable
data class BattleTagDto(
    val tagId: Long?,
    val name: String?,
    val type: String?
)

// Data -> Domain
fun BattleDetailDto.toDomainModel() = BattleDetailBoard(
    battleInfo = battleInfo?.toDomainModel() ?: BattleInfoBoard.empty(),
    description = description ?: "",
    shareUrl = shareUrl ?: "",
    userVoteStatus = userVoteStatus ?: "NONE",
    currentStep = currentStep ?: "",
    categoryTags = categoryTags?.map { it.toDomainModel() } ?: emptyList(),
    philosopherTags = philosopherTags?.map { it.toDomainModel() } ?: emptyList(),
    valueTags = valueTags?.map { it.toDomainModel() } ?: emptyList()
)

fun BattleInfoDto.toDomainModel() = BattleInfoBoard(
    battleId = battleId.toString() ?: "1",
    title = title ?: "",
    summary = summary ?: "",
    thumbnailUrl = thumbnailUrl ?: "",
    viewCount = viewCount ?: 0,
    participantsCount = participantsCount ?: 0,
    audioDuration = audioDuration ?: 0,
    tags = tags?.map { it.toDomainModel() } ?: emptyList(),
    options = options?.map { it.toDomainModel() } ?: emptyList()
)

fun BattleOptionDto.toDomainModel() = BattleOptionBoard(
    optionId = optionId.toString() ?: "1",
    title = title ?: "",
    stance = stance ?: "",
    representative = representative ?: "",
    imageUrl = imageUrl ?: "",
    tags = tags?.map { it.toDomainModel() } ?: emptyList()
)

fun BattleTagDto.toDomainModel() = BattleTagBoard(
    tagId = tagId.toString() ?: "",
    name = name ?: "",
    type = type ?: ""
)