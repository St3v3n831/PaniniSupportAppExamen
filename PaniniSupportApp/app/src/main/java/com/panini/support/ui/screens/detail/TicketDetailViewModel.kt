package com.panini.support.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.panini.support.core.ApiResult
import com.panini.support.core.UiState
import com.panini.support.data.repository.TicketRepository
import com.panini.support.domain.model.Ticket
import com.panini.support.domain.model.TicketPriority
import com.panini.support.domain.model.TicketStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TicketDetailViewModel(
    private val ticketRepository: TicketRepository,
    private val ticketId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Ticket>>(UiState.Loading)
    val uiState: StateFlow<UiState<Ticket>> = _uiState.asStateFlow()

    private val _actionState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val actionState: StateFlow<UiState<Unit>> = _actionState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    init {
        loadTicket()
    }

    fun loadTicket() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val r = ticketRepository.getTicketById(ticketId)) {
                is ApiResult.Success -> UiState.Success(r.data)
                is ApiResult.Error   -> UiState.Error(r.message)
            }
        }
    }

    fun updateStatus(status: TicketStatus, comment: String? = null) {
        viewModelScope.launch {
            _actionState.value = UiState.Loading
            when (val r = ticketRepository.updateTicketStatus(ticketId, status, comment)) {
                is ApiResult.Success -> {
                    _uiState.value    = UiState.Success(r.data)
                    _actionState.value = UiState.Idle
                    _snackbarMessage.emit("Estado actualizado a: ${status.label}")
                }
                is ApiResult.Error -> {
                    _actionState.value = UiState.Error(r.message)
                    _snackbarMessage.emit(r.message)
                }
            }
        }
    }

    fun updatePriority(priority: TicketPriority, reason: String? = null) {
        viewModelScope.launch {
            _actionState.value = UiState.Loading
            when (val r = ticketRepository.updateTicketPriority(ticketId, priority, reason)) {
                is ApiResult.Success -> {
                    _uiState.value    = UiState.Success(r.data)
                    _actionState.value = UiState.Idle
                    _snackbarMessage.emit("Prioridad actualizada a: ${priority.label}")
                }
                is ApiResult.Error -> {
                    _actionState.value = UiState.Error(r.message)
                    _snackbarMessage.emit(r.message)
                }
            }
        }
    }
}

class TicketDetailViewModelFactory(
    private val ticketRepository: TicketRepository,
    private val ticketId: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TicketDetailViewModel(ticketRepository, ticketId) as T
}
