package com.picke.data.feature.classroom.model

import com.picke.data.common.error.requireField
import com.picke.domain.feature.classroom.model.ClassTopicCandidateBoard
import com.picke.domain.feature.classroom.model.ClassTopicDraftBoard
import com.picke.domain.feature.classroom.model.ClassTopicOptionBoard
import com.picke.domain.feature.classroom.model.ClassTopicVersionBoard
import kotlinx.serialization.Serializable

@Serializable
data class ClassTopicDraftRequestDto(
    val topicSeed: String,
    val difficulty: String,
    val categories: List<String>
)

@Serializable
data class ClassTopicDraftResponseDto(
    val draftId: Long,
    val suggestStatus: String,
    val clarificationMessage: String?,
    val batchNo: Int,
    val remainingSuggestions: Int,
    val candidates: List<ClassTopicCandidateDto>
)

@Serializable
data class ClassTopicCandidateDto(
    val candidateId: Long,
    val question: String?,
    val description: String?,
    val category: String,
    val difficulty: String,
    val options: List<ClassTopicOptionDto>
)

@Serializable
data class ClassTopicSelectionRequestDto(
    val batchNo: Int,
    val candidateId: Long
)

@Serializable
data class ClassTopicVersionResponseDto(
    val topicVersionId: Long,
    val draftId: Long,
    val versionNo: Int,
    val source: String,
    val question: String,
    val description: String,
    val category: String,
    val difficulty: String,
    val options: List<ClassTopicOptionDto>
)

@Serializable
data class ClassTopicOptionDto(
    val position: String,
    val label: String?,
    val rationale: String?,
    val philosopher: String,
    val philosopherName: String,
    val philosopherImageKey: String
)

fun ClassTopicDraftResponseDto.toDomainModel() = ClassTopicDraftBoard(
    draftId = draftId,
    suggestStatus = suggestStatus,
    clarificationMessage = clarificationMessage,
    batchNo = batchNo,
    remainingSuggestions = remainingSuggestions,
    candidates = candidates.map { it.toDomainModel() }
)

fun ClassTopicCandidateDto.toDomainModel() = ClassTopicCandidateBoard(
    candidateId = candidateId,
    question = question.requireField("ClassTopicCandidateDto.question"),
    description = description.requireField("ClassTopicCandidateDto.description"),
    category = category,
    difficulty = difficulty,
    options = options.map { it.toDomainModel() }
)

fun ClassTopicVersionResponseDto.toDomainModel() = ClassTopicVersionBoard(
    topicVersionId = topicVersionId,
    draftId = draftId,
    versionNo = versionNo,
    source = source,
    question = question,
    description = description,
    category = category,
    difficulty = difficulty,
    options = options.map { it.toDomainModel() }
)

fun ClassTopicOptionDto.toDomainModel() = ClassTopicOptionBoard(
    position = position,
    label = label.requireField("ClassTopicOptionDto.label"),
    rationale = rationale.requireField("ClassTopicOptionDto.rationale"),
    philosopher = philosopher,
    philosopherName = philosopherName,
    philosopherImageKey = philosopherImageKey
)