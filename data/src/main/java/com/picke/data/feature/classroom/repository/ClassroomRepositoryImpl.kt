package com.picke.data.feature.classroom.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.model.toUnitResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.classroom.datasource.ClassroomApi
import com.picke.data.feature.classroom.model.ClassCreateRequestDto
import com.picke.data.feature.classroom.model.ClassDebateReplyRequestDto
import com.picke.data.feature.classroom.model.ClassJoinRequestDto
import com.picke.data.feature.classroom.model.ClassMemberNameUpdateRequestDto
import com.picke.data.feature.classroom.model.ClassTicketRegisterRequestDto
import com.picke.data.feature.classroom.model.ClassTopicDraftRequestDto
import com.picke.data.feature.classroom.model.ClassTopicSelectionRequestDto
import com.picke.data.feature.classroom.model.ClassUpdateRequestDto
import com.picke.data.feature.classroom.model.toDomainModel
import com.picke.data.feature.pollquiz.model.VoteRequestDto
import com.picke.domain.feature.classroom.model.ClassBattleDetailBoard
import com.picke.domain.feature.classroom.model.ClassBattleRecommendationBoard
import com.picke.domain.feature.classroom.model.ClassDebateBoard
import com.picke.domain.feature.classroom.model.ClassDetailBoard
import com.picke.domain.feature.classroom.model.ClassJoinPreviewBoard
import com.picke.domain.feature.classroom.model.ClassMemberBoard
import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard
import com.picke.domain.feature.classroom.model.ClassTopicDraftBoard
import com.picke.domain.feature.classroom.model.ClassTopicVersionBoard
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

    override suspend fun previewClassTicket(code: String): Result<ClassTicketPreviewBoard> =
        apiCall {
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

    override suspend fun createClass(
        name: String,
        battleId: Long?,
        topicVersionId: Long?,
        deadline: String?,
        commentRequired: Boolean,
        ownerParticipates: Boolean
    ): Result<ClassDetailBoard> = apiCall {
        val request = ClassCreateRequestDto(
            name = name,
            battleId = battleId,
            topicVersionId = topicVersionId,
            deadline = deadline,
            commentRequired = commentRequired,
            ownerParticipates = ownerParticipates
        )
        classroomApi.createClass(request)
            .toResult("클래스를 만들지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun updateClass(
        classId: Long,
        name: String?,
        deadline: String?
    ): Result<ClassDetailBoard> = apiCall {
        classroomApi.updateClass(classId, ClassUpdateRequestDto(name, deadline))
            .toResult("클래스 정보를 수정하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun deleteClass(classId: Long): Result<Unit> = apiCall {
        classroomApi.deleteClass(classId)
            .toUnitResult("클래스를 삭제하지 못했습니다.")
    }

    override suspend fun getClassBattleRecommendations(
        difficulty: String,
        categories: List<String>,
        keyword: String?
    ): Result<List<ClassBattleRecommendationBoard>> = apiCall {
        classroomApi.getClassBattleRecommendations(difficulty, categories, keyword)
            .toResult("추천 배틀을 불러오지 못했습니다.")
            .map { response -> response.items?.map { it.toDomainModel() } ?: emptyList() }
    }

    override suspend fun createClassTopicDraft(
        topicSeed: String,
        difficulty: String,
        categories: List<String>
    ): Result<ClassTopicDraftBoard> = apiCall {
        val request = ClassTopicDraftRequestDto(topicSeed, difficulty, categories)
        classroomApi.createClassTopicDraft(request)
            .toResult("주제 초안을 만들지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun suggestClassTopics(draftId: Long): Result<ClassTopicDraftBoard> = apiCall {
        classroomApi.suggestClassTopics(draftId)
            .toResult("AI 주제를 추천받지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun selectClassTopicCandidate(
        draftId: Long,
        batchNo: Int,
        candidateId: Long
    ): Result<ClassTopicVersionBoard> = apiCall {
        val request = ClassTopicSelectionRequestDto(batchNo, candidateId)
        classroomApi.selectClassTopicCandidate(draftId, request)
            .toResult("주제 후보를 선택하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun previewClassJoin(inviteCode: String): Result<ClassJoinPreviewBoard> =
        apiCall {
            classroomApi.previewClassJoin(inviteCode)
                .toResult("참여 코드를 확인하지 못했습니다.")
                .map { it.toDomainModel() }
        }

    override suspend fun joinClass(
        inviteCode: String,
        displayName: String
    ): Result<ClassDetailBoard> = apiCall {
        classroomApi.joinClass(ClassJoinRequestDto(inviteCode, displayName))
            .toResult("클래스에 참여하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun getClassMembers(classId: Long): Result<List<ClassMemberBoard>> = apiCall {
        classroomApi.getClassMembers(classId)
            .toResult("클래스 명단을 불러오지 못했습니다.")
            .map { members -> members.map { it.toDomainModel() } }
    }

    override suspend fun updateMyDisplayName(
        classId: Long,
        displayName: String
    ): Result<ClassMemberBoard> = apiCall {
        classroomApi.updateMyDisplayName(classId, ClassMemberNameUpdateRequestDto(displayName))
            .toResult("이름을 변경하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun leaveClass(classId: Long): Result<Unit> = apiCall {
        classroomApi.leaveClass(classId)
            .toUnitResult("클래스에서 나가지 못했습니다.")
    }

    override suspend fun removeMember(classId: Long, memberId: Long): Result<Unit> = apiCall {
        classroomApi.removeMember(classId, memberId)
            .toUnitResult("멤버를 내보내지 못했습니다.")
    }

    override suspend fun submitClassPreVote(classId: Long, optionId: Long): Result<Unit> = apiCall {
        classroomApi.submitClassPreVote(classId, VoteRequestDto(optionId))
            .toResult("사전 투표를 하지 못했습니다.")
            .map { }
    }

    override suspend fun submitClassPostVote(classId: Long, optionId: Long): Result<Unit> =
        apiCall {
            classroomApi.submitClassPostVote(classId, VoteRequestDto(optionId))
                .toResult("최종 투표를 하지 못했습니다.")
                .map { }
        }

    override suspend fun completeClassTts(classId: Long): Result<Unit> = apiCall {
        classroomApi.completeClassTts(classId)
            .toUnitResult("시나리오 청취 완료를 저장하지 못했습니다.")
    }

    override suspend fun getClassDebate(classId: Long): Result<ClassDebateBoard> = apiCall {
        classroomApi.getClassDebate(classId)
            .toResult("AI 토론을 불러오지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun startClassDebate(classId: Long): Result<ClassDebateBoard> = apiCall {
        classroomApi.startClassDebate(classId)
            .toResult("AI 토론을 시작하지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun submitClassDebateReply(
        classId: Long,
        roundIndex: Int,
        content: String,
        requestKey: String
    ): Result<ClassDebateBoard> = apiCall {
        classroomApi.submitClassDebateReply(
            classId,
            ClassDebateReplyRequestDto(
                roundIndex = roundIndex,
                content = content,
                requestKey = requestKey
            )
        )
            .toResult("AI 토론 반박을 보내지 못했습니다.")
            .map { it.toDomainModel() }
    }

    override suspend fun retryClassDebate(classId: Long): Result<ClassDebateBoard> = apiCall {
        classroomApi.retryClassDebate(classId)
            .toResult("AI 토론 주장을 다시 만들지 못했습니다.")
            .map { it.toDomainModel() }
    }
}