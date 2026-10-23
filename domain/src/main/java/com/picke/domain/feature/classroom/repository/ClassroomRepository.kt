package com.picke.domain.feature.classroom.repository

import com.picke.domain.feature.classroom.model.ClassBattleDetailBoard
import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.model.ClassJoinPreviewBoard
import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard
import com.picke.domain.feature.classroom.model.MyClassBoard

interface ClassroomRepository {
    suspend fun getMyClassTicket(): Result<ClassTicketBoard>
    suspend fun previewClassTicket(code: String): Result<ClassTicketPreviewBoard>
    suspend fun registerClassTicket(code: String): Result<ClassTicketBoard>
    suspend fun getMyClasses(): Result<List<MyClassBoard>>
    suspend fun createClass(
        name: String,
        battleId: Long,
        deadline: String?,
        commentRequired: Boolean,
        ownerParticipates: Boolean
    ): Result<ClassDetailBoard>

    suspend fun getClassDetail(classId: Long): Result<ClassDetailBoard>
    suspend fun getClassBattle(classId: Long): Result<ClassBattleDetailBoard>
    suspend fun deleteClass(classId: Long): Result<Unit>
    suspend fun previewClassJoin(inviteCode: String): Result<ClassJoinPreviewBoard>
    suspend fun joinClass(inviteCode: String, displayName: String): Result<ClassDetailBoard>
}