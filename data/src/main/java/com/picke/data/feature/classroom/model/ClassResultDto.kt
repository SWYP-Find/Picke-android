package com.picke.data.feature.classroom.model

import com.picke.data.common.error.mapItemOrNull
import com.picke.data.common.error.requireField
import com.picke.domain.feature.classroom.model.ClassResultBoard
import com.picke.domain.feature.classroom.model.ClassResultFeedbackBoard
import com.picke.domain.feature.classroom.model.ClassResultFeedbackEvidenceBoard
import com.picke.domain.feature.classroom.model.ClassResultFeedbackPointBoard
import com.picke.domain.feature.classroom.model.ClassResultInsightBoard
import com.picke.domain.feature.classroom.model.ClassResultInsightEvidenceBoard
import com.picke.domain.feature.classroom.model.ClassResultMyActivityBoard
import com.picke.domain.feature.classroom.model.ClassResultMyActivityCommentBoard
import com.picke.domain.feature.classroom.model.ClassResultMyReportBoard
import com.picke.domain.feature.classroom.model.ClassResultOptionBoard
import com.picke.domain.feature.classroom.model.ClassResultReportOptionBoard
import com.picke.domain.feature.classroom.model.ClassResultStatsBoard
import kotlinx.serialization.Serializable

private const val INSIGHT_STATE_SUCCEEDED = "SUCCEEDED"

@Serializable
data class ClassResultResponseDto(
    val classId: Long,
    val myRole: String,
    val closeAt: String?,
    val closed: Boolean,
    val viewableUntil: String?,
    val statsBasis: String,
    val cutoffAt: String?,
    val stats: ClassResultStatsDto,
    val myReportState: String?,
    val insightState: String?
)

@Serializable
data class ClassResultStatsDto(
    val activeMemberCount: Int,
    val completedCount: Int,
    val participationRate: Double,
    val commentWriterCount: Int,
    val commentRate: Double,
    val postVoteCount: Int,
    val options: List<ClassResultOptionDto>
)

@Serializable
data class ClassResultOptionDto(
    val optionId: Long,
    val title: String,
    val stance: String?,
    val preVoteCount: Int,
    val postVoteCount: Int,
    val postVoteRatio: Double
)

@Serializable
data class ClassResultMyReportResponseDto(
    val classId: Long,
    val state: String,
    val closeAt: String?,
    val preOption: ClassResultReportOptionDto?,
    val postOption: ClassResultReportOptionDto?,
    val perspectiveCount: Int,
    val commentCount: Int,
    val feedback: ClassResultFeedbackDto?,
    val generatedAt: String?
)

@Serializable
data class ClassResultReportOptionDto(
    val optionId: Long,
    val title: String,
    val stance: String?
)

@Serializable
data class ClassResultFeedbackDto(
    val headline: String,
    val judgmentBasis: String,
    val strength: ClassResultFeedbackPointDto?,
    val nextFocus: ClassResultFeedbackPointDto?,
    val nextQuestion: String,
    val evidence: List<ClassResultFeedbackEvidenceDto>,
    val analysisStatus: String,
    val coverage: String,
    val excludedCount: Int
)

@Serializable
data class ClassResultFeedbackPointDto(
    val title: String,
    val body: String
)

@Serializable
data class ClassResultFeedbackEvidenceDto(
    val field: String,
    val sourceType: String,
    val sourceId: Long,
    val quote: String,
    val content: String,
    val createdAt: String
)

@Serializable
data class ClassResultInsightResponseDto(
    val classId: Long,
    val state: String,
    val retryable: Boolean,
    val cutoffAt: String?,
    val generatedAt: String?,
    val insightText: String?,
    val coverage: String?,
    val excludedCount: Int,
    val evidence: List<ClassResultInsightEvidenceDto>
)

@Serializable
data class ClassResultInsightEvidenceDto(
    val sourceType: String,
    val id: Long,
    val optionId: Long,
    val stance: String?,
    val content: String,
    val createdAt: String
)

@Serializable
data class ClassResultMyActivityResponseDto(
    val classId: Long,
    val receivedLikeCount: Int,
    val commentCount: Int,
    val comments: List<ClassResultMyActivityCommentDto>,
    val nextCursor: String?,
    val hasNext: Boolean
)

@Serializable
data class ClassResultMyActivityCommentDto(
    val commentId: Long,
    val perspectiveId: Long,
    val perspectiveTitle: String,
    val stance: String?,
    val content: String,
    val likeCount: Int,
    val createdAt: String
)

fun ClassResultResponseDto.toDomainModel() = ClassResultBoard(
    classId = classId,
    myRole = myRole,
    closeAt = closeAt.requireField("ClassResultResponseDto.closeAt"),
    closed = closed,
    viewableUntil = viewableUntil.requireField("ClassResultResponseDto.viewableUntil"),
    statsBasis = statsBasis,
    cutoffAt = cutoffAt,
    stats = stats.toDomainModel(),
    myReportState = myReportState,
    insightState = insightState
)

fun ClassResultStatsDto.toDomainModel() = ClassResultStatsBoard(
    activeMemberCount = activeMemberCount,
    completedCount = completedCount,
    participationRate = participationRate,
    commentWriterCount = commentWriterCount,
    commentRate = commentRate,
    postVoteCount = postVoteCount,
    options = options.map { it.toDomainModel() }
)

fun ClassResultOptionDto.toDomainModel() = ClassResultOptionBoard(
    optionId = optionId,
    title = title,
    stance = stance.requireField("ClassResultOptionDto.stance"),
    preVoteCount = preVoteCount,
    postVoteCount = postVoteCount,
    postVoteRatio = postVoteRatio
)

fun ClassResultMyReportResponseDto.toDomainModel() = ClassResultMyReportBoard(
    classId = classId,
    state = state,
    closeAt = closeAt.requireField("ClassResultMyReportResponseDto.closeAt"),
    preOption = preOption?.toDomainModel(),
    postOption = postOption?.toDomainModel(),
    perspectiveCount = perspectiveCount,
    commentCount = commentCount,
    feedback = feedback?.toDomainModel(),
    generatedAt = generatedAt
)

fun ClassResultReportOptionDto.toDomainModel() = ClassResultReportOptionBoard(
    optionId = optionId,
    title = title,
    stance = stance.requireField("ClassResultReportOptionDto.stance")
)

fun ClassResultFeedbackDto.toDomainModel() = ClassResultFeedbackBoard(
    headline = headline,
    judgmentBasis = judgmentBasis,
    strength = strength?.toDomainModel(),
    nextFocus = nextFocus?.toDomainModel(),
    nextQuestion = nextQuestion,
    evidence = evidence.map { it.toDomainModel() },
    analysisStatus = analysisStatus,
    coverage = coverage,
    excludedCount = excludedCount
)

fun ClassResultFeedbackPointDto.toDomainModel() = ClassResultFeedbackPointBoard(
    title = title,
    body = body
)

fun ClassResultFeedbackEvidenceDto.toDomainModel() = ClassResultFeedbackEvidenceBoard(
    field = field,
    sourceType = sourceType,
    sourceId = sourceId,
    quote = quote,
    content = content,
    createdAt = createdAt
)

fun ClassResultInsightResponseDto.toDomainModel() = ClassResultInsightBoard(
    classId = classId,
    state = state,
    retryable = retryable,
    cutoffAt = cutoffAt,
    generatedAt = generatedAt,
    insightText = if (state == INSIGHT_STATE_SUCCEEDED) {
        insightText.requireField("ClassResultInsightResponseDto.insightText")
    } else {
        insightText
    },
    coverage = coverage,
    excludedCount = excludedCount,
    evidence = evidence.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } }
)

fun ClassResultInsightEvidenceDto.toDomainModel() = ClassResultInsightEvidenceBoard(
    sourceType = sourceType,
    id = id,
    optionId = optionId,
    stance = stance.requireField("ClassResultInsightEvidenceDto.stance"),
    content = content,
    createdAt = createdAt
)

fun ClassResultMyActivityResponseDto.toDomainModel() = ClassResultMyActivityBoard(
    classId = classId,
    receivedLikeCount = receivedLikeCount,
    commentCount = commentCount,
    comments = comments.mapNotNull { item -> mapItemOrNull { item.toDomainModel() } },
    nextCursor = nextCursor,
    hasNext = hasNext
)

fun ClassResultMyActivityCommentDto.toDomainModel() = ClassResultMyActivityCommentBoard(
    commentId = commentId,
    perspectiveId = perspectiveId,
    perspectiveTitle = perspectiveTitle,
    stance = stance.requireField("ClassResultMyActivityCommentDto.stance"),
    content = content,
    likeCount = likeCount,
    createdAt = createdAt
)