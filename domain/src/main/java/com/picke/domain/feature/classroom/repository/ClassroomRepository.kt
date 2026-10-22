package com.picke.domain.feature.classroom.repository

import com.picke.domain.feature.classroom.model.ClassTicketBoard
import com.picke.domain.feature.classroom.model.ClassTicketPreviewBoard
import com.picke.domain.feature.classroom.model.MyClassBoard

interface ClassroomRepository {
    suspend fun getMyClassTicket(): Result<ClassTicketBoard>
    suspend fun previewClassTicket(code: String): Result<ClassTicketPreviewBoard>
    suspend fun registerClassTicket(code: String): Result<ClassTicketBoard>
    suspend fun getMyClasses(): Result<List<MyClassBoard>>
}