package com.picke.data.feature.scenario.repository

import com.picke.data.common.error.toReportedFailure
import com.picke.data.common.model.toResult
import com.picke.data.feature.scenario.datasource.ScenarioApi
import com.picke.data.feature.scenario.model.toDomainModel
import com.picke.domain.feature.scenario.model.ScenarioBoard
import com.picke.domain.feature.scenario.repository.ScenarioRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ScenarioRepositoryImpl @Inject constructor(
    private val scenarioApi: ScenarioApi
) : ScenarioRepository {
    override suspend fun fetchBattleScenario(battleId: String): Result<ScenarioBoard> {
        return try {
            scenarioApi.getScenario(battleId)
                .toResult("시나리오를 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            e.toReportedFailure()
        }
    }
}