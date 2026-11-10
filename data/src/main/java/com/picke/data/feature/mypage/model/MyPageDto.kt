package com.picke.data.feature.mypage.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.mypage.model.CreditHistoryItem
import com.picke.domain.feature.mypage.model.CreditHistoryPage
import com.picke.domain.feature.mypage.model.FavoriteTopic
import com.picke.domain.feature.mypage.model.MyBattleRecordItem
import com.picke.domain.feature.mypage.model.MyBattleRecordPage
import com.picke.domain.feature.mypage.model.MyContentActivityAuthor
import com.picke.domain.feature.mypage.model.MyContentActivityItem
import com.picke.domain.feature.mypage.model.MyContentActivityPage
import com.picke.domain.feature.mypage.model.MyPageInfoBoard
import com.picke.domain.feature.mypage.model.MyPhilosopher
import com.picke.domain.feature.mypage.model.MyProfile
import com.picke.domain.feature.mypage.model.MyRecapBoard
import com.picke.domain.feature.mypage.model.MyTier
import com.picke.domain.feature.mypage.model.NotificationSettingsBoard
import com.picke.domain.feature.mypage.model.PreferenceReport
import com.picke.domain.feature.mypage.model.ProfileUpdateBoard
import com.picke.domain.feature.mypage.model.RecapScores
import kotlinx.serialization.Serializable

// --- [Request DTOs] ---
@Serializable
data class NotificationSettingsDto(
    val newBattleEnabled: Boolean,
    val battleResultEnabled: Boolean,
    val commentReplyEnabled: Boolean,
    val newCommentEnabled: Boolean,
    val contentLikeEnabled: Boolean,
    val marketingEventEnabled: Boolean
)

@Serializable
data class ProfileUpdateRequestDto(
    val nickname: String,
    val characterType: String
)

// --- [Response DTOs] ---
@Serializable
data class MyBattleRecordItemDto(
    val battleId: String,
    val recordId: String,
    val voteSide: String?,
    val category: String?,
    val title: String,
    val summary: String?,
    val createdAt: String
)

@Serializable
data class MyBattleRecordPageDto(
    val items: List<MyBattleRecordItemDto>,
    val nextOffset: Int?,
    val hasNext: Boolean
)

@Serializable
data class MyContentActivityAuthorDto(
    val userTag: String,
    val nickname: String?,
    val characterType: String,
    val characterImageUrl: String
)

@Serializable
data class MyContentActivityItemDto(
    val activityId: String,
    val activityType: String,
    val perspectiveId: String,
    val battleId: String,
    val battleTitle: String?,
    val author: MyContentActivityAuthorDto,
    val voteSide: String?,
    val optionTitle: String?,
    val content: String,
    val likeCount: Int,
    val createdAt: String
)

@Serializable
data class MyContentActivityPageDto(
    val items: List<MyContentActivityItemDto>,
    val nextOffset: Int?,
    val hasNext: Boolean
)

@Serializable
data class MyProfileDto(
    val userTag: String,
    val nickname: String?,
    val characterType: String,
    val characterLabel: String,
    val characterImageUrl: String,
    val mannerTemperature: Double?
)

@Serializable
data class MyPhilosopherDto(
    val philosopherType: String,
    val philosopherLabel: String,
    val typeName: String,
    val description: String,
    val keywordTags: List<String>?,
    val imageUrl: String
)

@Serializable
data class MyTierDto(
    val tierCode: String,
    val tierLabel: String,
    val currentPoint: Int
)

@Serializable
data class MyPageInfoDto(
    val profile: MyProfileDto,
    val philosopher: MyPhilosopherDto?,
    val tier: MyTierDto
)

@Serializable
data class ProfileUpdateResponseDto(
    val userTag: String,
    val nickname: String?,
    val characterType: String,
    val updatedAt: String
)

@Serializable
data class FavoriteTopicDto(
    val rank: Int,
    val tagName: String,
    val participationCount: Int
)

@Serializable
data class RecapScoresDto(
    val principle: Int,
    val reason: Int,
    val individual: Int,
    val change: Int,
    val inner: Int,
    val ideal: Int
)

@Serializable
data class PreferenceReportDto(
    val totalParticipation: Int,
    val opinionChanges: Int,
    val battleWinRate: Int,
    val favoriteTopics: List<FavoriteTopicDto>
)

@Serializable
data class MyRecapDto(
    val myCard: MyPhilosopherDto,
    val bestMatchCard: MyPhilosopherDto,
    val worstMatchCard: MyPhilosopherDto,
    val scores: RecapScoresDto,
    val preferenceReport: PreferenceReportDto
)

@Serializable
data class CreditHistoryItemDto(
    val id: Long,
    val creditType: String,
    val amount: Int,
    val referenceId: Long,
    val createdAt: String
)

@Serializable
data class CreditHistoryPageDto(
    val items: List<CreditHistoryItemDto>,
    val nextOffset: Int?,
    val hasNext: Boolean
)

fun CreditHistoryItemDto.toDomainModel() = CreditHistoryItem(
    id = id,
    creditType = creditType,
    amount = amount,
    referenceId = referenceId,
    createdAt = createdAt.take(10)
)

fun CreditHistoryPageDto.toDomainModel() = CreditHistoryPage(
    items = items.map { it.toDomainModel() },
    nextOffset = nextOffset,
    hasNext = hasNext
)

// Mappers DTO -> Domain
fun MyBattleRecordItemDto.toDomainModel() = MyBattleRecordItem(
    battleId = battleId,
    recordId = recordId,
    voteSide = voteSide.requireField("MyBattleRecordItemDto.voteSide"),
    category = category.requireField("MyBattleRecordItemDto.category"),
    title = title,
    summary = summary.requireField("MyBattleRecordItemDto.summary"),
    createdAt = createdAt.take(10)
)

fun MyBattleRecordPageDto.toDomainModel() = MyBattleRecordPage(
    items = items.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
    nextOffset = nextOffset,
    hasNext = hasNext
)

fun MyContentActivityItemDto.toDomainModel() = MyContentActivityItem(
    activityId = activityId,
    activityType = activityType,
    perspectiveId = perspectiveId,
    battleId = battleId,
    battleTitle = battleTitle.requireField("MyContentActivityItemDto.battleTitle"),
    author = MyContentActivityAuthor(
        userTag = author.userTag,
        nickname = author.nickname.requireField("MyContentActivityAuthorDto.nickname"),
        characterType = author.characterType,
        characterImageUrl = author.characterImageUrl
    ),
    voteSide = voteSide,
    optionTitle = optionTitle,
    content = content,
    likeCount = likeCount,
    createdAt = createdAt
)

fun MyContentActivityPageDto.toDomainModel() = MyContentActivityPage(
    items = items.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
    nextOffset = nextOffset,
    hasNext = hasNext
)

fun MyPageInfoDto.toDomainModel() = MyPageInfoBoard(
    profile = MyProfile(
        userTag = profile.userTag,
        nickname = profile.nickname.requireField("MyProfileDto.nickname"),
        characterType = profile.characterType,
        characterLabel = profile.characterLabel,
        characterImageUrl = profile.characterImageUrl,
        mannerTemperature = profile.mannerTemperature.requireField("MyProfileDto.mannerTemperature")
    ),
    philosopher = philosopher?.toDomainModel(),
    tier = MyTier(
        tierCode = tier.tierCode,
        tierLabel = tier.tierLabel,
        currentPoint = tier.currentPoint
    )
)

fun MyPhilosopherDto.toDomainModel() = MyPhilosopher(
    philosopherType = philosopherType,
    philosopherLabel = philosopherLabel,
    typeName = typeName,
    description = description,
    keywordTags = keywordTags ?: emptyList(),
    imageUrl = imageUrl
)

fun NotificationSettingsDto.toDomainModel() = NotificationSettingsBoard(
    newBattleEnabled = newBattleEnabled,
    battleResultEnabled = battleResultEnabled,
    commentReplyEnabled = commentReplyEnabled,
    newCommentEnabled = newCommentEnabled,
    contentLikeEnabled = contentLikeEnabled,
    marketingEventEnabled = marketingEventEnabled
)

// Request DTO 변환 (Domain -> DTO)
fun NotificationSettingsBoard.toDto() = NotificationSettingsDto(
    newBattleEnabled = newBattleEnabled,
    battleResultEnabled = battleResultEnabled,
    commentReplyEnabled = commentReplyEnabled,
    newCommentEnabled = newCommentEnabled,
    contentLikeEnabled = contentLikeEnabled,
    marketingEventEnabled = marketingEventEnabled
)

fun ProfileUpdateResponseDto.toDomainModel() = ProfileUpdateBoard(
    userTag = userTag,
    nickname = nickname.requireField("ProfileUpdateResponseDto.nickname"),
    characterType = characterType,
    updatedAt = updatedAt
)

fun MyRecapDto.toDomainModel() = MyRecapBoard(
    myCard = myCard.toDomainModel(),
    bestMatchCard = bestMatchCard.toDomainModel(),
    worstMatchCard = worstMatchCard.toDomainModel(),
    scores = RecapScores(
        principle = scores.principle,
        reason = scores.reason,
        individual = scores.individual,
        change = scores.change,
        inner = scores.inner,
        ideal = scores.ideal
    ),
    preferenceReport = PreferenceReport(
        totalParticipation = preferenceReport.totalParticipation,
        opinionChanges = preferenceReport.opinionChanges,
        battleWinRate = preferenceReport.battleWinRate,
        favoriteTopics = preferenceReport.favoriteTopics.map {
            FavoriteTopic(it.rank, it.tagName, it.participationCount)
        }
    )
)