package com.picke.domain.feature.classroom.model

data class ClassTopicDraftBoard(
    val draftId: Long,
    val suggestStatus: String,
    val clarificationMessage: String?,
    val batchNo: Int,
    val remainingSuggestions: Int,
    val candidates: List<ClassTopicCandidateBoard>
)

data class ClassTopicCandidateBoard(
    val candidateId: Long,
    val question: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val options: List<ClassTopicOptionBoard>
)

data class ClassTopicOptionBoard(
    val position: String,
    val label: String,
    val rationale: String,
    val philosopher: String,
    val philosopherName: String,
    val philosopherImageKey: String
)