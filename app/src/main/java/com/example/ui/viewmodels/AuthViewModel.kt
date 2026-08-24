package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.ApiClient
import com.example.data.api.UserProfile
import com.example.data.datastore.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Authenticated(val token: String) : AuthUiState
    data class ProfileIncomplete(val token: String) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val userPrefs = UserPreferences(application)

    val tokenFlow: Flow<String?> = userPrefs.authToken
    val isDarkModeFlow: Flow<Boolean> = userPrefs.isDarkMode

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    private val _isLoadingProfile = MutableStateFlow(false)
    val isLoadingProfile: StateFlow<Boolean> = _isLoadingProfile.asStateFlow()

    fun loginDemo() {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = ApiClient.service.login()
                val token = response.token
                if (!token.isNullOrEmpty()) {
                    userPrefs.saveAuth(token, response.username ?: "demo")
                    fetchProfile(token)
                } else {
                    _uiState.value = AuthUiState.Error("Failed to obtain authentication token from server")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Network connection error")
            }
        }
    }

    fun loginTelegram(initData: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = ApiClient.service.authTelegram(
                    com.example.data.api.TelegramAuthRequest(initData)
                )
                val token = response.token
                if (!token.isNullOrEmpty()) {
                    userPrefs.saveAuth(token)
                    if (response.user?.firstName.isNullOrEmpty()) {
                        _uiState.value = AuthUiState.ProfileIncomplete(token)
                    } else {
                        _userProfile.value = response.user
                        _uiState.value = AuthUiState.Authenticated(token)
                    }
                } else {
                    _uiState.value = AuthUiState.Error("Telegram authentication failed")
                }
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    fun completeProfile(token: String, firstName: String, lastName: String, birthDate: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                val response = ApiClient.service.updateProfile(
                    com.example.data.api.UpdateProfileRequest(
                        token = token,
                        firstName = firstName,
                        lastName = lastName,
                        birthDate = birthDate
                    )
                )
                _userProfile.value = response.user
                _uiState.value = AuthUiState.Authenticated(token)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.localizedMessage ?: "Failed to save profile")
            }
        }
    }

    fun fetchProfile(token: String) {
        viewModelScope.launch {
            _isLoadingProfile.value = true
            try {
                val response = ApiClient.service.getProfile(token)
                val user = response.user
                _userProfile.value = user
                if (user?.firstName.isNullOrEmpty() && user?.provider == "telegram") {
                    _uiState.value = AuthUiState.ProfileIncomplete(token)
                } else {
                    _uiState.value = AuthUiState.Authenticated(token)
                }
            } catch (e: Exception) {
                // Keep token, fallback
                _uiState.value = AuthUiState.Authenticated(token)
            } finally {
                _isLoadingProfile.value = false
            }
        }
    }

    fun updateAccountProfile(token: String, firstName: String, lastName: String, birthDate: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = ApiClient.service.updateProfile(
                    com.example.data.api.UpdateProfileRequest(
                        token = token,
                        firstName = firstName,
                        lastName = lastName,
                        birthDate = birthDate
                    )
                )
                _userProfile.value = response.user
                onSuccess()
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            userPrefs.setDarkMode(isDark)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefs.clearAuth()
            _uiState.value = AuthUiState.Idle
            _userProfile.value = null
        }
    }
}
