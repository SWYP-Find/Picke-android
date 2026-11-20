package com.picke.data.feature.proposal.repository

import com.picke.data.common.model.toResult
import com.picke.data.common.network.apiCall
import com.picke.data.feature.proposal.datasource.ProposalApi
import com.picke.data.feature.proposal.model.ProposalRequestDto
import com.picke.data.feature.proposal.model.toDomainModel
import com.picke.domain.feature.proposal.model.ProposalBoard
import com.picke.domain.feature.proposal.repository.ProposalRepository
import javax.inject.Inject

class ProposalRepositoryImpl @Inject constructor(
    private val proposalApi: ProposalApi
) : ProposalRepository {

    override suspend fun submitProposal(
        category: String,
        topic: String,
        positionA: String,
        positionB: String,
        description: String
    ): Result<ProposalBoard> = apiCall {
        val request = ProposalRequestDto(
            category = category,
            topic = topic,
            positionA = positionA,
            positionB = positionB,
            description = description
        )

        proposalApi.submitProposal(request)
            .toResult("주제를 제안하지 못했습니다.")
            .map { it.toDomainModel() }
    }
}