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

data class ClassResultMyReportBoard(
    val classId: Long,
    val state: String,
    val closeAt: String,
    val preOption: ClassResultReportOptionBoard?,
    val postOption: ClassResultReportOptionBoard?,
    val perspectiveCount: Int,
    val commentCount: Int,
    val feedback: ClassResultFeedbackBoard?,
    val generatedAt: String?
)

data class ClassResultReportOptionBoard(
    val optionId: Long,
    val title: String,
    val stance: String
)

data class ClassResultFeedbackBoard(
    val headline: String,
    val judgmentBasis: String,
    val strength: ClassResultFeedbackPointBoard?,
    val nextFocus: ClassResultFeedbackPointBoard?,
    val nextQuestion: String,
    val evidence: List<ClassResultFeedbackEvidenceBoard>,
    val analysisStatus: String,
    val coverage: String,
    val excludedCount: Int
)

data class ClassResultFeedbackPointBoard(
    val title: String,
    val body: String
)

data class ClassResultFeedbackEvidenceBoard(
    val field: String,
    val sourceType: String,
    val sourceId: Long,
    val quote: String,
    val content: String,
    val createdAt: String
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