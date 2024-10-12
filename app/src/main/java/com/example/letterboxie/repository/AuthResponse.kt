package com.example.letterboxie.repository

import com.google.firebase.auth.FirebaseUser

sealed class AuthResponse {
    data class Success(val user : FirebaseUser) : AuthResponse()
    data class Error(val errorMessage : String) : AuthResponse()
    data object Loading : AuthResponse()
}