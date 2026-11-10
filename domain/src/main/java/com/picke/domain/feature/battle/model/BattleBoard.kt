package com.picke.domain.feature.battle.model

data class BattleDetailBoard(
    val battleInfo: BattleInfoBoard,
    val description: String,
    val shareUrl: String,
    val userVoteStatus: String?,
    val currentStep: String,
    val categoryTags: List<BattleTagBoard>,
    val philosopherTags: List<BattleTagBoard>,
    val valueTags: List<BattleTagBoard>
)

data class BattleInfoBoard(
    val battleId: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String?,
    val viewCount: Int,
    val participantsCount: Int,
    val audioDuration: Int,
    val tags: List<BattleTagBoard>,
    val options: List<BattleOptionBoard>
)

data class BattleOptionBoard(
    val optionId: String,
    val title: String,
    val stance: String,
    val representative: String,
    val imageUrl: String?,
    val tags: List<BattleTagBoard>
)

data class BattleTagBoard(
    val tagId: String,
    val name: String,
    val type: String
)