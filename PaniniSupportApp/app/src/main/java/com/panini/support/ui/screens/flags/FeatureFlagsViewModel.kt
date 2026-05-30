package com.panini.support.ui.screens.flags

import androidx.lifecycle.ViewModel
import com.panini.support.core.FeatureFlags
import kotlinx.coroutines.flow.StateFlow

class FeatureFlagsViewModel : ViewModel() {

    val flags: StateFlow<FeatureFlags.Flags> = FeatureFlags.flags

    fun setShowCriticalCategory(enabled: Boolean) {
        FeatureFlags.update(flags.value.copy(showCriticalCategory = enabled))
    }

    fun setShowResolvedTickets(enabled: Boolean) {
        FeatureFlags.update(flags.value.copy(showResolvedTickets = enabled))
    }
}