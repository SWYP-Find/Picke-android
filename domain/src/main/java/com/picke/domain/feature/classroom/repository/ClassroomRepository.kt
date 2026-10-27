package com.picke.domain.feature.classroom.repository

import com.picke.domain.feature.classroom.model.ClassBattleDetailBoard
import com.picke.domain.feature.classroom.model.ClassBattleRecommendationBoard
import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.model.ClassJoinPreviewBoard
import com.picke.domain.feature.classroom.model.ClassMemberBoard
import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard
import com.picke.domain.feature.classroom.model.ClassTopicDraftBoard
import com.picke.domain.feature.classroom.model.ClassTopicVersionBoard
import com.picke.domain.feature.classroom.model.MyClassBoard

interface ClassroomRepository {
    suspend fun getMyClassTicket(): Result<ClassTicketBoard>
    suspend fun previewClassTicket(code: String): Result<ClassTicketPreviewBoard>
    suspend fun registerClassTicket(code: String): Result<ClassTicketBoard>
    suspend fun getMyClasses(): Result<List<MyClassBoard>>
    suspend fun getClassDetail(classId: Long): Result<ClassDetailBoard>
    suspend fun getClassBattle(classId: Long): Result<ClassBattleDetailBoard>
    suspend fun createClass(
        name: String,
        battleId: Long?,
        topicVersionId: Long?,
        deadline: String?,
        commentRequired: Boolean,
        ownerParticipates: Boolean
    ): Result<ClassDetailBoard>

    suspend fun updateClass(classId: Long, name: String?, deadline: String?): Result<ClassDetailBoard>
    suspend fun deleteClass(classId: Long): Result<Unit>
    suspend fun getClassBattleRecommendations(
        difficulty: String,
        categories: List<String>,
        keyword: String?
    ): Result<List<ClassBattleRecommendationBoard>>

    suspend fun createClassTopicDraft(
        topicSeed: String,
        difficulty: String,
        categories: List<String>
    ): Result<ClassTopicDraftBoard>

    suspend fun suggestClassTopics(draftId: Long): Result<ClassTopicDraftBoard>
    suspend fun selectClassTopicCandidate(
        draftId: Long,
        batchNo: Int,
        candidateId: Long
    ): Result<ClassTopicVersionBoard>

    suspend fun previewClassJoin(inviteCode: String): Result<ClassJoinPreviewBoard>
    suspend fun joinClass(inviteCode: String, displayName: String): Result<ClassDetailBoard>
    suspend fun getClassMembers(classId: Long): Result<List<ClassMemberBoard>>
    suspend fun updateMyDisplayName(classId: Long, displayName: String): Result<ClassMemberBoard>
    suspend fun leaveClass(classId: Long): Result<Unit>
    suspend fun removeMember(classId: Long, memberId: Long): Result<Unit>
    suspend fun submitClassPreVote(classId: Long, optionId: Long): Result<Unit>
    suspend fun submitClassPostVote(classId: Long, optionId: Long): Result<Unit>
    suspend fun completeClassTts(classId: Long): Result<Unit>
}