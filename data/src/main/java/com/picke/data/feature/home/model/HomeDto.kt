package com.picke.data.feature.home.model

import com.picke.domain.feature.home.model.ContentDomainType
import com.picke.domain.feature.home.model.ContentOption
import com.picke.domain.feature.home.model.HomeBestCommentBoard
import com.picke.domain.feature.home.model.HomeBoard
import com.picke.domain.feature.home.model.HomeContent

data class HomeResponseDto(
    val newNotice: Boolean?,
    val editorPicks: List<EditorPickDto>?,
    val trendingBattles: List<TrendingBattleDto>?,
    val bestBattles: List<BestBattleDto>?,
    val bestComments: List<BestCommentDto>?,
    val newBattles: List<NewBattleDto>?
)

data class TagDto(
    val tagId: Long?,
    val name: String?,
    val type: String?
)

data class EditorPickDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val optionATitle: String?,
    val optionBTitle: String?,
    val title: String?,
    val summary: String?,
    val tags: List<TagDto>?,
    val viewCount: Int?
)

data class TrendingBattleDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val title: String?,
    val tags: List<TagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

data class BestBattleDto(
    val battleId: Long?,
    val philosopherA: String?,
    val philosopherB: String?,
    val title: String?,
    val tags: List<TagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

// TODO: API 연동 - 홈 API에 bestComments가 추가되면 필드 이름 확인
data class BestCommentDto(
    val battleId: Long?,
    val battleTitle: String?,
    val optionTitle: String?,
    val content: String?
)

data class NewBattleDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val title: String?,
    val summary: String?,
    val optionATitle: String?,
    val philosopherA: String?,
    val philosopherAImageUrl: String?,
    val optionBTitle: String?,
    val philosopherB: String?,
    val philosopherBImageUrl: String?,
    val tags: List<TagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

fun HomeResponseDto.toDomainModel() = HomeBoard(
    editorPicks = editorPicks?.map { it.toDomainModel() } ?: emptyList(),
    trendingBattles = trendingBattles?.map { it.toDomainModel() } ?: emptyList(),
    bestBattles = bestBattles?.map { it.toDomainModel() } ?: emptyList(),
    bestComments = bestComments?.mapNotNull { it.toDomainModel() } ?: emptyList(),
    newBattles = newBattles?.map { it.toDomainModel() } ?: emptyList()
)

fun EditorPickDto.toDomainModel() = HomeContent(
    contentId = this.battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = this.title ?: "",
    summary = this.summary ?: "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    viewCount = this.viewCount ?: 0,
    audioDuration = 0,
    tags = this.tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption(text = this.optionATitle ?: "", philosopherName = null, imageUrl = null),
        ContentOption(text = this.optionBTitle ?: "", philosopherName = null, imageUrl = null)
    )
)

fun TrendingBattleDto.toDomainModel() = HomeContent(
    contentId = this.battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = this.title ?: "",
    summary = "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    viewCount = this.viewCount ?: 0,
    audioDuration = this.audioDuration ?: 0,
    tags = this.tags?.mapNotNull { it.name } ?: emptyList(),
    options = emptyList()
)

fun NewBattleDto.toDomainModel() = HomeContent(
    contentId = this.battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = this.title ?: "",
    summary = this.summary ?: "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    viewCount = this.viewCount ?: 0,
    audioDuration = this.audioDuration ?: 0,
    tags = this.tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption(
            text = this.optionATitle ?: "",
            philosopherName = this.philosopherA ?: "",
            imageUrl = this.philosopherAImageUrl
        ),
        ContentOption(
            text = this.optionBTitle ?: "",
            philosopherName = this.philosopherB ?: "",
            imageUrl = this.philosopherBImageUrl
        )
    )
)

fun BestCommentDto.toDomainModel(): HomeBestCommentBoard? {
    val battleId = battleId ?: return null
    return HomeBestCommentBoard(
        battleId = battleId,
        battleTitle = battleTitle ?: "",
        optionTitle = optionTitle ?: "",
        content = content ?: ""
    )
}

fun BestBattleDto.toDomainModel() = HomeContent(
    contentId = this.battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = this.title ?: "",
    summary = "",
    thumbnailUrl = "",
    viewCount = this.viewCount ?: 0,
    audioDuration = this.audioDuration ?: 0,
    tags = this.tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption(text = "", philosopherName = this.philosopherA ?: "", imageUrl = null),
        ContentOption(text = "", philosopherName = this.philosopherB ?: "", imageUrl = null)
    )
)