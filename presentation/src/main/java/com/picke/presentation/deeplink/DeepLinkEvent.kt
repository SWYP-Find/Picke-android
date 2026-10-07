package com.picke.presentation.deeplink

sealed class DeepLinkEvent {
    data class GoToBattle(val battleId: String) : DeepLinkEvent()
    data class GoToTodayBattle(val battleId: String) : DeepLinkEvent()
    data class GoToReport(val reportId: String) : DeepLinkEvent()
    data object GoToAlarm : DeepLinkEvent()
    data class GoToPerspective(val perspectiveId: String, val commentId: String?) : DeepLinkEvent()
}