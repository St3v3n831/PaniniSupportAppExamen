package com.panini.support.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.panini.support.core.ApiResult
import com.panini.support.core.UiState
import com.panini.support.data.repository.AuthRepository
import com.panini.support.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val uiState: StateFlow<UiState<User>> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = UiState.Error("Por favor complete todos los campos.")
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = when (val result = authRepository.login(email, password)) {
                is ApiResult.Success -> UiState.Success(result.data)
                is ApiResult.Error   -> UiState.Error(result.message)
            }
        }
    }

    fun resetState() { _uiState.value = UiState.Idle }
}

class LoginViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        LoginViewModel(authRepository) as T
}
