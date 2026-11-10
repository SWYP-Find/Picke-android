package com.picke.domain.feature.home.model

data class HomeBoard(
    val newNotice: Boolean,
    val editorPicks: List<HomeEditorPickBoard>,
    val trendingBattles: List<HomeTrendingBattleBoard>,
    val bestBattles: List<HomeBestBattleBoard>,
    val newBattles: List<HomeNewBattleBoard>
)

data class HomeEditorPickBoard(
    val battleId: Long,
    val thumbnailUrl: String,
    val optionATitle: String,
    val optionBTitle: String,
    val title: String,
    val summary: String,
    val tags: List<String>,
    val viewCount: Int,
    val audioDuration: Int
)

data class HomeTrendingBattleBoard(
    val badgeLabel: String,
    val battleId: Long,
    val thumbnailUrl: String,
    val title: String,
    val tags: List<String>,
    val audioDuration: Int,
    val viewCount: Int
)

data class HomeBestBattleBoard(
    val badgeLabel: String,
    val battleId: Long,
    val philosopherA: String,
    val philosopherB: String,
    val title: String,
    val tags: List<String>,
    val audioDuration: Int,
    val viewCount: Int
)

data class HomeNewBattleBoard(
    val battleId: Long,
    val thumbnailUrl: String?,
    val title: String,
    val summary: String,
    val philosopherA: String,
    val optionATitle: String,
    val philosopherAImageUrl: String?,
    val philosopherB: String,
    val optionBTitle: String,
    val philosopherBImageUrl: String?,
    val tags: List<String>,
    val audioDuration: Int,
    val viewCount: Int
)