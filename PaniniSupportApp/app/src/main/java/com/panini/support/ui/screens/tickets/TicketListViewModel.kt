package com.panini.support.ui.screens.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.panini.support.core.ApiResult
import com.panini.support.core.UiState
import com.panini.support.data.repository.TicketEvent
import com.panini.support.data.repository.TicketRepository
import com.panini.support.domain.model.Ticket
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TicketListViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Ticket>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Ticket>>> = _uiState.asStateFlow()

    // Mensaje de snackbar para feedback puntual
    private val _snackbarMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    init {
        loadTickets()
        observeTicketEvents()
    }

    fun loadTickets() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = ticketRepository.getTickets()) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Error   -> UiState.Error(result.message)
            }
        }
    }

    /**
     * Suscripción a eventos de dominio — Req. 5.1
     * La pantalla reacciona automáticamente sin recargar manualmente.
     */
    private fun observeTicketEvents() {
        viewModelScope.launch {
            ticketRepository.ticketEvents.collect { event ->
                when (event) {
                    // Escenario 1: Nuevo ticket → se agrega y reordena por prioridad
                    is TicketEvent.TicketCreated -> {
                        val current = (_uiState.value as? UiState.Success)?.data ?: emptyList()
                        val updated = (current + event.ticket).sortedBy { it.priority.order }
                        _uiState.value = UiState.Success(updated)
                        _snackbarMessage.emit("Ticket ${event.ticket.id} creado exitosamente.")
                    }
                    // Escenario 2: Prioridad actualizada → reposiciona en lista
                    is TicketEvent.PriorityUpdated -> {
                        val current = (_uiState.value as? UiState.Success)?.data ?: emptyList()
                        val updated = current
                            .map { if (it.id == event.ticket.id) event.ticket else it }
                            .sortedBy { it.priority.order }
                        _uiState.value = UiState.Success(updated)
                        _snackbarMessage.emit("Prioridad de ${event.ticket.id} actualizada.")
                    }
                    // Estado actualizado → refleja cambio sin mover posición
                    is TicketEvent.StatusUpdated -> {
                        val current = (_uiState.value as? UiState.Success)?.data ?: emptyList()
                        val updated = current.map { if (it.id == event.ticket.id) event.ticket else it }
                        _uiState.value = UiState.Success(updated)
                    }
                }
            }
        }
    }
}

class TicketListViewModelFactory(
    private val ticketRepository: TicketRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        TicketListViewModel(ticketRepository) as T
}
