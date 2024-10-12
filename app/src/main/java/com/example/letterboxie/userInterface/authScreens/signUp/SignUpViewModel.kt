package com.example.letterboxie.userInterface.authScreens.signUp

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.models.firestore.UserProfile
import com.example.letterboxie.repository.AuthRepository
import com.example.letterboxie.repository.AuthResponse
import com.example.letterboxie.repository.FirestoreRepository
import com.example.letterboxie.repository.NetworkResponse
import com.example.letterboxie.utilities.Constants.USER_PROFILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val authRepository : AuthRepository,
    private val firestoreRepository : FirestoreRepository, private val sp : SharedPreferences,
    private val simpleDateTimeFormatter : SimpleDateFormat) : ViewModel() {
    val signUpUIState = MutableLiveData < SignUpUIState > ()
    val userProfileSave = MutableLiveData < SignUpUIState > ()

    fun register(username : String, email : String, password : String, rememberMe : Boolean) {
        viewModelScope.launch {
            authRepository.register(email, password).collectLatest { response ->
                when (response) {
                    is AuthResponse.Success -> {
                        signUpUIState.value = SignUpUIState.Success
                        saveCurrentUserID(response.user.uid)
                        val currentDateTime = simpleDateTimeFormatter.format(Date(System.currentTimeMillis()))
                        val currentUserProfile = UserProfile(documentID = response.user.uid,
                            username = username, createdAt = currentDateTime)
                        saveUserProfile(currentUserProfile)
                        saveRememberMe(rememberMe)
                    }
                    is AuthResponse.Error -> signUpUIState.value = SignUpUIState.Error(response.errorMessage)
                    is AuthResponse.Loading -> signUpUIState.value = SignUpUIState.Loading
                }
            }
        }
    }

    private fun saveUserProfile(userProfile : UserProfile) {
        viewModelScope.launch {
            firestoreRepository.saveDocument(userDocumentID = userProfile.documentID, collectionPath = USER_PROFILE,
                childDocumentID = userProfile.documentID, data = userProfile).collectLatest { response ->
                userProfileSave.value = when (response) {
                    is NetworkResponse.Success -> SignUpUIState.Success
                    is NetworkResponse.Error -> SignUpUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> SignUpUIState.Loading
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

sealed class SignUpUIState {
    data object Success : SignUpUIState()
    data class Error(val errorMessage : String) : SignUpUIState()
    data object Loading : SignUpUIState()
}