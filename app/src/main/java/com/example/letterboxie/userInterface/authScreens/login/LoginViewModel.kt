package com.example.letterboxie.userInterface.authScreens.login

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.repository.AuthRepository
import com.example.letterboxie.repository.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository : AuthRepository,
    private val sp : SharedPreferences) : ViewModel() {
    val loginUIState = MutableLiveData < LoginUIState > ()

    fun authenticate(email : String, password : String, rememberMe : Boolean) {
        viewModelScope.launch {
            authRepository.authenticate(email, password).collectLatest { response ->
                when (response) {
                    is AuthResponse.Success -> {
                        loginUIState.value = LoginUIState.Success
                        saveCurrentUserID(response.user.uid)
                        saveRememberMe(rememberMe)
                    }
                    is AuthResponse.Error -> loginUIState.value = LoginUIState.Error(response.errorMessage)
                    is AuthResponse.Loading -> loginUIState.value = LoginUIState.Loading
                }
            }
        }
    }

    private fun saveRememberMe(rememberMe : Boolean) {
        val editor = sp.edit()
        editor.putBoolean("remember_me", rememberMe)
        editor.apply()
    }

    private fun saveCurrentUserID(currentUserID : String) {
        val editor = sp.edit()
        editor.putString("current_user_ID", currentUserID)
        editor.apply()
    }
}

sealed class LoginUIState {
    data object Success : LoginUIState()
    data class Error(val errorMessage : String) : LoginUIState()
    data object Loading : LoginUIState()
}