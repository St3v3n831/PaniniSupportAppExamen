package com.panini.support.ui.screens.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.panini.support.core.ApiResult
import com.panini.support.core.UiState
import com.panini.support.data.repository.TicketRepository
import com.panini.support.data.remote.mock.MockDataSource
import com.panini.support.domain.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CreateTicketFormState(
    val title:          String         = "",
    val description:    String         = "",
    val priority:       TicketPriority = TicketPriority.MEDIUM,
    val category:       TicketCategory = TicketCategory.INVENTORY_SHORTAGE,
    val supplierId:     String         = MockDataSource.suppliers.first().first,
    val supplierName:   String         = MockDataSource.suppliers.first().second,
    val affectedRegion: String         = "",
    val titleError:     String?        = null,
    val descError:      String?        = null
)

class CreateTicketViewModel(
    private val ticketRepository: TicketRepository
) : ViewModel() {

    private val _form = MutableStateFlow(CreateTicketFormState())
    val form: StateFlow<CreateTicketFormState> = _form.asStateFlow()

    private val _submitState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val submitState: StateFlow<UiState<Unit>> = _submitState.asStateFlow()

    private val _submitted = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val submitted: SharedFlow<Unit> = _submitted.asSharedFlow()

    val suppliers = MockDataSource.suppliers

    fun onTitleChange(value: String) {
        _form.value = _form.value.copy(title = value, titleError = null)
    }
    fun onDescriptionChange(value: String) {
        _form.value = _form.value.copy(description = value, descError = null)
    }
    fun onPriorityChange(value: TicketPriority) {
        _form.value = _form.value.copy(priority = value)
    }
    fun onCategoryChange(value: TicketCategory) {
        _form.value = _form.value.copy(category = value)
    }
    fun onSupplierChange(id: String, name: String) {
        _form.value = _form.value.copy(supplierId = id, supplierName = name)
    }
    fun onRegionChange(value: String) {
        _form.value = _form.value.copy(affectedRegion = value)
    }

    private fun validate(): Boolean {
        val f = _form.value
        var valid = true
        var updated = f
        if (f.title.isBlank()) {
            updated = updated.copy(titleError = "El título es obligatorio.")
            valid = false
        } else if (f.title.length > 120) {
            updated = updated.copy(titleError = "Máximo 120 caracteres.")
            valid = false
        }
        if (f.description.isBlank()) {
            updated = updated.copy(descError = "La descripción es obligatoria.")
            valid = false
        }
        _form.value = updated
        return valid
    }

    fun submit() {
        if (!validate()) return
        val f = _form.value
        viewModelScope.launch {
            _submitState.value = UiState.Loading
            when (val r = ticketRepository.createTicket(
                title          = f.title,
                description    = f.description,
                priority       = f.priority,
                category       = f.category,
                supplierId     = f.supplierId,
                supplierName   = f.supplierName,
                affectedRegion = f.affectedRegion
            )) {
                is ApiResult.Success -> {
                    _submitState.value = UiState.Idle
                    _submitted.emit(Unit)
                }
                is ApiResult.Error -> {
                    _submitState.value = UiState.Error(r.message)
                }
            }
        }
    }
}

class CreateTicketViewModelFactory(
    private val ticketRepository: TicketRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        CreateTicketViewModel(ticketRepository) as T
}
