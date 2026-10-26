package com.picke.data.feature.classroom.model

import com.picke.domain.feature.classroom.model.ClassTopicCandidateBoard
import com.picke.domain.feature.classroom.model.ClassTopicDraftBoard
import com.picke.domain.feature.classroom.model.ClassTopicOptionBoard

data class ClassTopicDraftRequestDto(
    val topicSeed: String,
    val difficulty: String,
    val categories: List<String>
)

data class ClassTopicDraftResponseDto(
    val draftId: Long?,
    val suggestStatus: String?,
    val clarificationMessage: String?,
    val batchNo: Int?,
    val remainingSuggestions: Int?,
    val candidates: List<ClassTopicCandidateDto>?
)

data class ClassTopicCandidateDto(
    val candidateId: Long?,
    val question: String?,
    val description: String?,
    val category: String?,
    val difficulty: String?,
    val options: List<ClassTopicOptionDto>?
)

data class ClassTopicOptionDto(
    val position: String?,
    val label: String?,
    val rationale: String?,
    val philosopher: String?,
    val philosopherName: String?,
    val philosopherImageKey: String?
)

fun ClassTopicDraftResponseDto.toDomainModel() = ClassTopicDraftBoard(
    draftId = draftId ?: 0L,
    suggestStatus = suggestStatus ?: "",
    clarificationMessage = clarificationMessage,
    batchNo = batchNo ?: 0,
    remainingSuggestions = remainingSuggestions ?: 0,
    candidates = candidates?.map { it.toDomainModel() } ?: emptyList()
)

fun ClassTopicCandidateDto.toDomainModel() = ClassTopicCandidateBoard(
    candidateId = candidateId ?: 0L,
    question = question ?: "",
    description = description ?: "",
    category = category ?: "",
    difficulty = difficulty ?: "",
    options = options?.map { it.toDomainModel() } ?: emptyList()
)

fun ClassTopicOptionDto.toDomainModel() = ClassTopicOptionBoard(
    position = position ?: "",
    label = label ?: "",
    rationale = rationale ?: "",
    philosopher = philosopher ?: "",
    philosopherName = philosopherName ?: "",
    philosopherImageKey = philosopherImageKey ?: ""
)