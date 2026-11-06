package com.picke.data.feature.home.model

import com.picke.domain.feature.home.model.ContentDomainType
import com.picke.domain.feature.home.model.ContentOption
import com.picke.domain.feature.home.model.HomeBestCommentBoard
import com.picke.domain.feature.home.model.HomeBoard
import com.picke.domain.feature.home.model.HomeContent
import kotlinx.serialization.Serializable

@Serializable
data class HomeResponseDto(
    val newNotice: Boolean?,
    val editorPicks: List<EditorPickDto>?,
    val trendingBattles: List<TrendingBattleDto>?,
    val bestBattles: List<BestBattleDto>?,
    val bestComments: List<BestCommentDto>?,
    val newBattles: List<NewBattleDto>?
)

@Serializable
data class TagDto(
    val tagId: Long?,
    val name: String?,
    val type: String?
)

@Serializable
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

@Serializable
data class TrendingBattleDto(
    val battleId: Long?,
    val thumbnailUrl: String?,
    val title: String?,
    val tags: List<TagDto>?,
    val audioDuration: Int?,
    val viewCount: Int?
)

@Serializable
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
@Serializable
data class BestCommentDto(
    val battleId: Long?,
    val battleTitle: String?,
    val optionTitle: String?,
    val content: String?
)

@Serializable
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
    contentId = battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = title ?: "",
    summary = summary ?: "",
    thumbnailUrl = thumbnailUrl ?: "",
    viewCount = viewCount ?: 0,
    audioDuration = 0,
    tags = tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption(text = optionATitle ?: "", philosopherName = null, imageUrl = null),
        ContentOption(text = optionBTitle ?: "", philosopherName = null, imageUrl = null)
    )
)

fun TrendingBattleDto.toDomainModel() = HomeContent(
    contentId = battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = title ?: "",
    summary = "",
    thumbnailUrl = thumbnailUrl ?: "",
    viewCount = viewCount ?: 0,
    audioDuration = audioDuration ?: 0,
    tags = tags?.mapNotNull { it.name } ?: emptyList(),
    options = emptyList()
)

fun NewBattleDto.toDomainModel() = HomeContent(
    contentId = battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = title ?: "",
    summary = summary ?: "",
    thumbnailUrl = thumbnailUrl ?: "",
    viewCount = viewCount ?: 0,
    audioDuration = audioDuration ?: 0,
    tags = tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption(
            text = optionATitle ?: "",
            philosopherName = philosopherA ?: "",
            imageUrl = philosopherAImageUrl
        ),
        ContentOption(
            text = optionBTitle ?: "",
            philosopherName = philosopherB ?: "",
            imageUrl = philosopherBImageUrl
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
    contentId = battleId?.toString() ?: "",
    type = ContentDomainType.BATTLE,
    title = title ?: "",
    summary = "",
    thumbnailUrl = "",
    viewCount = viewCount ?: 0,
    audioDuration = audioDuration ?: 0,
    tags = tags?.mapNotNull { it.name } ?: emptyList(),
    options = listOf(
        ContentOption(text = "", philosopherName = philosopherA ?: "", imageUrl = null),
        ContentOption(text = "", philosopherName = philosopherB ?: "", imageUrl = null)
    )
)