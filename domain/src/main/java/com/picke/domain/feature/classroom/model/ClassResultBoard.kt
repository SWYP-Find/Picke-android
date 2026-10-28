package com.picke.domain.feature.classroom.model

data class ClassResultBoard(
    val classId: Long,
    val myRole: String,
    val closeAt: String,
    val closed: Boolean,
    val viewableUntil: String,
    val statsBasis: String,
    val cutoffAt: String?,
    val stats: ClassResultStatsBoard,
    val myReportState: String?,
    val insightState: String?
)

data class ClassResultStatsBoard(
    val activeMemberCount: Int,
    val completedCount: Int,
    val participationRate: Double,
    val commentWriterCount: Int,
    val commentRate: Double,
    val postVoteCount: Int,
    val options: List<ClassResultOptionBoard>
)

data class ClassResultOptionBoard(
    val optionId: Long,
    val title: String,
    val stance: String,
    val preVoteCount: Int,
    val postVoteCount: Int,
    val postVoteRatio: Double
)

data class ClassResultInsightBoard(
    val classId: Long,
    val state: String,
    val retryable: Boolean,
    val cutoffAt: String?,
    val generatedAt: String?,
    val insightText: String,
    val coverage: String,
    val excludedCount: Int,
    val evidence: List<ClassResultInsightEvidenceBoard>
)

data class ClassResultInsightEvidenceBoard(
    val sourceType: String,
    val id: Long,
    val optionId: Long,
    val stance: String,
    val content: String,
    val createdAt: String
)