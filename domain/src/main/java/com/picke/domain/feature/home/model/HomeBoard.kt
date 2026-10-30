package com.picke.domain.feature.home.model

enum class ContentDomainType {
    BATTLE, VOTE, QUIZ, UNKNOWN
}

data class HomeBoard(
    val editorPicks: List<HomeContent>,
    val trendingBattles: List<HomeContent>,
    val bestBattles: List<HomeContent>,
    val bestComments: List<HomeBestCommentBoard>,
    val newBattles: List<HomeContent>
)

data class HomeBestCommentBoard(
    val battleId: Long,
    val battleTitle: String,
    val optionTitle: String,
    val content: String
)

data class HomeContent(
    val contentId: String,
    val type: ContentDomainType,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val viewCount: Int,
    val audioDuration: Int,
    val tags: List<String>,
    val options: List<ContentOption>
)

data class ContentOption(
    val text: String,
    val philosopherName: String?,
    val imageUrl: String?
)