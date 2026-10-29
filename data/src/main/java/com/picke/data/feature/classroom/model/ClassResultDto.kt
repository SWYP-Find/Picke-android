package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassResultBoard
import com.picke.domain.feature.classroom.model.ClassResultFeedbackBoard
import com.picke.domain.feature.classroom.model.ClassResultFeedbackEvidenceBoard
import com.picke.domain.feature.classroom.model.ClassResultFeedbackPointBoard
import com.picke.domain.feature.classroom.model.ClassResultInsightBoard
import com.picke.domain.feature.classroom.model.ClassResultInsightEvidenceBoard
import com.picke.domain.feature.classroom.model.ClassResultMyReportBoard
import com.picke.domain.feature.classroom.model.ClassResultOptionBoard
import com.picke.domain.feature.classroom.model.ClassResultReportOptionBoard
import com.picke.domain.feature.classroom.model.ClassResultStatsBoard

data class ClassResultResponseDto(
    val classId: Long?,
    val myRole: String?,
    val closeAt: String?,
    val closed: Boolean?,
    val viewableUntil: String?,
    val statsBasis: String?,
    val cutoffAt: String?,
    val stats: ClassResultStatsDto?,
    val myReportState: String?,
    val insightState: String?
)

data class ClassResultStatsDto(
    val activeMemberCount: Int?,
    val completedCount: Int?,
    val participationRate: Double?,
    val commentWriterCount: Int?,
    val commentRate: Double?,
    val postVoteCount: Int?,
    val options: List<ClassResultOptionDto>?
)

data class ClassResultOptionDto(
    val optionId: Long?,
    val title: String?,
    val stance: String?,
    val preVoteCount: Int?,
    val postVoteCount: Int?,
    val postVoteRatio: Double?
)

data class ClassResultMyReportResponseDto(
    val classId: Long?,
    val state: String?,
    val closeAt: String?,
    val preOption: ClassResultReportOptionDto?,
    val postOption: ClassResultReportOptionDto?,
    val perspectiveCount: Int?,
    val commentCount: Int?,
    val feedback: ClassResultFeedbackDto?,
    val generatedAt: String?
)

data class ClassResultReportOptionDto(
    val optionId: Long?,
    val title: String?,
    val stance: String?
)

data class ClassResultFeedbackDto(
    val headline: String?,
    val judgmentBasis: String?,
    val strength: ClassResultFeedbackPointDto?,
    val nextFocus: ClassResultFeedbackPointDto?,
    val nextQuestion: String?,
    val evidence: List<ClassResultFeedbackEvidenceDto>?,
    val analysisStatus: String?,
    val coverage: String?,
    val excludedCount: Int?
)

data class ClassResultFeedbackPointDto(
    val title: String?,
    val body: String?
)

data class ClassResultFeedbackEvidenceDto(
    val field: String?,
    val sourceType: String?,
    val sourceId: Long?,
    val quote: String?,
    val content: String?,
    val createdAt: String?
)

data class ClassResultInsightResponseDto(
    val classId: Long?,
    val state: String?,
    val retryable: Boolean?,
    val cutoffAt: String?,
    val generatedAt: String?,
    val insightText: String?,
    val coverage: String?,
    val excludedCount: Int?,
    val evidence: List<ClassResultInsightEvidenceDto>?
)

data class ClassResultInsightEvidenceDto(
    val sourceType: String?,
    val id: Long?,
    val optionId: Long?,
    val stance: String?,
    val content: String?,
    val createdAt: String?
)

fun ClassResultResponseDto.toDomainModel() = ClassResultBoard(
    classId = classId ?: 0L,
    myRole = myRole ?: "",
    closeAt = closeAt ?: "",
    closed = closed ?: false,
    viewableUntil = viewableUntil ?: "",
    statsBasis = statsBasis ?: "",
    cutoffAt = cutoffAt,
    stats = stats?.toDomainModel() ?: ClassResultStatsBoard(
        activeMemberCount = 0,
        completedCount = 0,
        participationRate = 0.0,
        commentWriterCount = 0,
        commentRate = 0.0,
        postVoteCount = 0,
        options = emptyList()
    ),
    myReportState = myReportState,
    insightState = insightState
)

fun ClassResultStatsDto.toDomainModel() = ClassResultStatsBoard(
    activeMemberCount = activeMemberCount ?: 0,
    completedCount = completedCount ?: 0,
    participationRate = participationRate ?: 0.0,
    commentWriterCount = commentWriterCount ?: 0,
    commentRate = commentRate ?: 0.0,
    postVoteCount = postVoteCount ?: 0,
    options = options?.map { it.toDomainModel() } ?: emptyList()
)

fun ClassResultOptionDto.toDomainModel() = ClassResultOptionBoard(
    optionId = optionId ?: 0L,
    title = title ?: "",
    stance = stance ?: "",
    preVoteCount = preVoteCount ?: 0,
    postVoteCount = postVoteCount ?: 0,
    postVoteRatio = postVoteRatio ?: 0.0
)

fun ClassResultMyReportResponseDto.toDomainModel() = ClassResultMyReportBoard(
    classId = classId ?: 0L,
    state = state ?: "",
    closeAt = closeAt ?: "",
    preOption = preOption?.toDomainModel(),
    postOption = postOption?.toDomainModel(),
    perspectiveCount = perspectiveCount ?: 0,
    commentCount = commentCount ?: 0,
    feedback = feedback?.toDomainModel(),
    generatedAt = generatedAt
)

fun ClassResultReportOptionDto.toDomainModel() = ClassResultReportOptionBoard(
    optionId = optionId ?: 0L,
    title = title ?: "",
    stance = stance ?: ""
)

fun ClassResultFeedbackDto.toDomainModel() = ClassResultFeedbackBoard(
    headline = headline ?: "",
    judgmentBasis = judgmentBasis ?: "",
    strength = strength?.toDomainModel(),
    nextFocus = nextFocus?.toDomainModel(),
    nextQuestion = nextQuestion ?: "",
    evidence = evidence?.map { it.toDomainModel() } ?: emptyList(),
    analysisStatus = analysisStatus ?: "",
    coverage = coverage ?: "",
    excludedCount = excludedCount ?: 0
)

fun ClassResultFeedbackPointDto.toDomainModel() = ClassResultFeedbackPointBoard(
    title = title ?: "",
    body = body ?: ""
)

fun ClassResultFeedbackEvidenceDto.toDomainModel() = ClassResultFeedbackEvidenceBoard(
    field = field ?: "",
    sourceType = sourceType ?: "",
    sourceId = sourceId ?: 0L,
    quote = quote ?: "",
    content = content ?: "",
    createdAt = createdAt ?: ""
)

fun ClassResultInsightResponseDto.toDomainModel() = ClassResultInsightBoard(
    classId = classId ?: 0L,
    state = state ?: "",
    retryable = retryable ?: false,
    cutoffAt = cutoffAt,
    generatedAt = generatedAt,
    insightText = insightText ?: "",
    coverage = coverage ?: "",
    excludedCount = excludedCount ?: 0,
    evidence = evidence?.map { it.toDomainModel() } ?: emptyList()
)

fun ClassResultInsightEvidenceDto.toDomainModel() = ClassResultInsightEvidenceBoard(
    sourceType = sourceType ?: "",
    id = id ?: 0L,
    optionId = optionId ?: 0L,
    stance = stance ?: "",
    content = content ?: "",
    createdAt = createdAt ?: ""
)