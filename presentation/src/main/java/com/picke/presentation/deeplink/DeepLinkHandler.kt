package com.picke.presentation.deeplink

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkHandler @Inject constructor() {

    private val _pendingEvent = MutableStateFlow<DeepLinkEvent?>(null)
    val pendingEvent: StateFlow<DeepLinkEvent?> = _pendingEvent.asStateFlow()

    fun submit(event: DeepLinkEvent) {
        _pendingEvent.value = event
    }

    fun consume() {
        _pendingEvent.value = null
    }
}