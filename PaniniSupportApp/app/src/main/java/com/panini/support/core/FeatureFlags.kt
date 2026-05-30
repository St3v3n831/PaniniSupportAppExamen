package com.panini.support.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FeatureFlags {

    data class Flags(
        val showCriticalCategory: Boolean = true,
        val showResolvedTickets: Boolean = true
    )

    private val _flags = MutableStateFlow(Flags())
    val flags: StateFlow<Flags> = _flags.asStateFlow()

    fun update(updated: Flags) {
        _flags.value = updated
    }
}