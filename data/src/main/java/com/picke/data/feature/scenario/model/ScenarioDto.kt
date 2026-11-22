package com.picke.data.feature.scenario.model

import com.picke.data.common.error.requireField
import com.picke.domain.feature.scenario.model.ScenarioBoard
import com.picke.domain.feature.scenario.model.ScenarioNode
import com.picke.domain.feature.scenario.model.ScenarioOption
import com.picke.domain.feature.scenario.model.ScenarioPhilosopher
import com.picke.domain.feature.scenario.model.ScenarioScript
import com.picke.domain.feature.scenario.model.SpeakerType
import kotlinx.serialization.Serializable

@Serializable
data class ScenarioPhilosopherDto(
    val name: String?,
    val stance: String?,
    val quote: String?,
    val imageUrl: String?
)

@Serializable
data class ScenarioResponseDto(
    val battleId: Long,
    val title: String?,
    val philosophers: List<ScenarioPhilosopherDto>?,
    val isInteractive: Boolean,
    val startNodeId: Long?,
    val recommendedPathKey: String,
    val audios: Map<String, String>,
    val nodes: List<ScenarioNodeDto>
)

@Serializable
data class ScenarioNodeDto(
    val nodeId: Long,
    val nodeName: String?,
    val audioDuration: Int?,
    val autoNextNodeId: Long?,
    val scripts: List<ScenarioScriptDto>,
    val interactiveOptions: List<ScenarioOptionDto>
)

@Serializable
data class ScenarioScriptDto(
    val scriptId: Long,
    val startTimeMs: Long?,
    val speakerType: String,
    val speakerName: String?,
    val text: String
)

@Serializable
data class ScenarioOptionDto(
    val optionId: Long?,
    val label: String?,
    val nextNodeId: Long?
)

fun ScenarioResponseDto.toDomainModel(): ScenarioBoard =
    ScenarioBoard(
        battleId = battleId.toString(),
        title = title.requireField("ScenarioResponseDto.title"),
        philosophers = philosophers?.map { it.toDomainModel() } ?: emptyList(),
        isInteractive = isInteractive,
        startNodeId = startNodeId.requireField("ScenarioResponseDto.startNodeId").toString(),
        recommendedPathKey = recommendedPathKey,
        audios = audios,
        nodes = nodes.map { it.toDomainModel() }
    )

private fun ScenarioPhilosopherDto.toDomainModel(): ScenarioPhilosopher =
    ScenarioPhilosopher(
        name = name,
        stance = stance,
        quote = quote,
        imageUrl = imageUrl
    )

private fun ScenarioNodeDto.toDomainModel(): ScenarioNode =
    ScenarioNode(
        nodeId = nodeId.toString(),
        nodeName = nodeName.requireField("ScenarioNodeDto.nodeName"),
        audioDuration = audioDuration.requireField("ScenarioNodeDto.audioDuration"),
        autoNextNodeId = autoNextNodeId?.toString(),
        scripts = scripts.map { it.toDomainModel() },
        interactiveOptions = interactiveOptions.mapNotNull { it.toDomainModel() }
    )

private fun ScenarioScriptDto.toDomainModel(): ScenarioScript {
    val speakerType = runCatching { SpeakerType.valueOf(speakerType) }.getOrDefault(SpeakerType.UNKNOWN)
    return ScenarioScript(
        scriptId = scriptId.toString(),
        startTimeMs = startTimeMs.requireField("ScenarioScriptDto.startTimeMs"),
        speakerType = speakerType,
        speakerName = if (speakerType == SpeakerType.NARRATOR) {
            speakerName
        } else {
            speakerName.requireField("ScenarioScriptDto.speakerName")
        },
        text = text
    )
}

private fun ScenarioOptionDto.toDomainModel(): ScenarioOption? {
    val optionId = optionId ?: return null
    return ScenarioOption(
        optionId = optionId,
        label = label.requireField("ScenarioOptionDto.label"),
        nextNodeId = nextNodeId.requireField("ScenarioOptionDto.nextNodeId").toString()
    )
}