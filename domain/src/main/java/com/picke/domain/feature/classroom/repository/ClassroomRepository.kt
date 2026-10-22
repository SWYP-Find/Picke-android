package com.picke.domain.feature.classroom.repository

import com.picke.domain.feature.classroom.model.ClassTicketBoard

interface ClassroomRepository {
    suspend fun getMyClassTicket(): Result<ClassTicketBoard>
}