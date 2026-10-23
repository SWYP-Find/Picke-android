package com.picke.data.feature.classroom.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.model.toUnitResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.classroom.datasource.ClassroomApi
import com.picke.data.feature.classroom.model.ClassCreateRequestDto
import com.picke.data.feature.classroom.model.ClassJoinRequestDto
import com.picke.data.feature.classroom.model.ClassTicketRegisterRequestDto
import com.picke.data.feature.classroom.model.toDomainModel
import com.picke.domain.feature.classroom.model.ClassBattleDetailBoard
import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.model.ClassJoinPreviewBoard
import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard
import com.picke.domain.feature.classroom.model.MyClassBoard
import com.picke.domain.feature.classroom.repository.ClassroomRepository
import javax.inject.Inject

class ClassroomRepositoryImpl @Inject constructor(
    private val classroomApi: ClassroomApi
) : ClassroomRepository {

    override suspend fun getMyClassTicket(): Result<ClassTicketBoard> = apiCall {
        classroomApi.getMyClassTicket()
            .toResult("내 이용권을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun previewClassTicket(code: String): Result<ClassTicketPreviewBoard> = apiCall {
        classroomApi.previewClassTicket(code)
            .toResult("이용권 코드를 확인하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun registerClassTicket(code: String): Result<ClassTicketBoard> = apiCall {
        classroomApi.registerClassTicket(ClassTicketRegisterRequestDto(code))
            .toResult("이용권을 등록하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getMyClasses(): Result<List<MyClassBoard>> = apiCall {
        classroomApi.getMyClasses()
            .toResult("내 클래스 목록을 불러오지 못했습니다.")
            .map { classes -> classes.map { it.toDomainModel() } }
    }

    override suspend fun createClass(
        name: String,
        battleId: Long,
        deadline: String?,
        commentRequired: Boolean,
        ownerParticipates: Boolean
    ): Result<ClassDetailBoard> = apiCall {
        val request = ClassCreateRequestDto(name, battleId, deadline, commentRequired, ownerParticipates)
        classroomApi.createClass(request)
            .toResult("클래스를 만들지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getClassDetail(classId: Long): Result<ClassDetailBoard> = apiCall {
        classroomApi.getClassDetail(classId)
            .toResult("클래스 상세를 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getClassBattle(classId: Long): Result<ClassBattleDetailBoard> = apiCall {
        classroomApi.getClassBattle(classId)
            .toResult("클래스 배틀을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun deleteClass(classId: Long): Result<Unit> = apiCall {
        classroomApi.deleteClass(classId)
            .toUnitResult("클래스를 삭제하지 못했습니다.")
    }

    override suspend fun previewClassJoin(inviteCode: String): Result<ClassJoinPreviewBoard> = apiCall {
        classroomApi.previewClassJoin(inviteCode)
            .toResult("참여 코드를 확인하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun joinClass(inviteCode: String, displayName: String): Result<ClassDetailBoard> = apiCall {
        classroomApi.joinClass(ClassJoinRequestDto(inviteCode, displayName))
            .toResult("클래스에 참여하지 못했습니다.")
            .map { it.toDomainModel() }
    }
}