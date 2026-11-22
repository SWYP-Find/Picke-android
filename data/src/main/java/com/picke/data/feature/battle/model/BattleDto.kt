package com.picke.data.feature.battle.model

import com.picke.data.common.error.requireField
import com.picke.domain.feature.battle.model.BattleDetailBoard
import com.picke.domain.feature.battle.model.BattleInfoBoard
import com.picke.domain.feature.battle.model.BattleOptionBoard
import com.picke.domain.feature.battle.model.BattleTagBoard
import kotlinx.serialization.Serializable

@Serializable
data class BattleDetailDto(
    val battleInfo: BattleInfoDto,
    val description: String?,
    val shareUrl: String,
    val userVoteStatus: String?,
    val currentStep: String,
    val categoryTags: List<BattleTagDto>,
    val philosopherTags: List<BattleTagDto>,
    val valueTags: List<BattleTagDto>
)

@Serializable
data class BattleInfoDto(
    val battleId: Long,
    val title: String,
    val summary: String?,
    val thumbnailUrl: String?,
    val viewCount: Int,
    val participantsCount: Int,
    val audioDuration: Int,
    val tags: List<BattleTagDto>,
    val options: List<BattleOptionDto>
)

@Serializable
data class BattleOptionDto(
    val optionId: Long,
    val title: String,
    val stance: String?,
    val representative: String?,
    val imageUrl: String?,
    val tags: List<BattleTagDto>
)

@Serializable
data class BattleTagDto(
    val tagId: Long,
    val name: String,
    val type: String
)

// Data -> Domain
fun BattleDetailDto.toDomainModel() = BattleDetailBoard(
    battleInfo = battleInfo.toDomainModel(),
    description = description.requireField("BattleDetailDto.description"),
    shareUrl = shareUrl,
    userVoteStatus = userVoteStatus,
    currentStep = currentStep,
    categoryTags = categoryTags.map { it.toDomainModel() },
    philosopherTags = philosopherTags.map { it.toDomainModel() },
    valueTags = valueTags.map { it.toDomainModel() }
)

fun BattleInfoDto.toDomainModel() = BattleInfoBoard(
    battleId = battleId.toString(),
    title = title,
    summary = summary.requireField("BattleInfoDto.summary"),
    thumbnailUrl = thumbnailUrl,
    viewCount = viewCount,
    participantsCount = participantsCount,
    audioDuration = audioDuration,
    tags = tags.map { it.toDomainModel() },
    options = options.map { it.toDomainModel() }
)

fun BattleOptionDto.toDomainModel() = BattleOptionBoard(
    optionId = optionId.toString(),
    title = title,
    stance = stance.requireField("BattleOptionDto.stance"),
    representative = representative.requireField("BattleOptionDto.representative"),
    imageUrl = imageUrl,
    tags = tags.map { it.toDomainModel() }
)

fun BattleTagDto.toDomainModel() = BattleTagBoard(
    tagId = tagId.toString(),
    name = name,
    type = type
)