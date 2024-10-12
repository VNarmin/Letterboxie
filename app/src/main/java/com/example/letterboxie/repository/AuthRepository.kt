package com.example.letterboxie.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(private val auth : FirebaseAuth) {
    fun authenticate(email : String, password: String) : Flow < AuthResponse > = flow {
        emit(AuthResponse.Loading)
        try {
            val response = auth.signInWithEmailAndPassword(email, password).await()
            val currentUser = response.user
            currentUser?.let { user ->
                emit(AuthResponse.Success(user))
            } ?: run { emit(AuthResponse.Error("Authentication Failed!!")) }
        } catch (exception : Exception) {
            emit(AuthResponse.Error(exception.localizedMessage ?: "Unknown Error!!"))
        }
    }.flowOn(Dispatchers.IO)

    fun register(email: String, password: String) : Flow< AuthResponse > = flow {
        emit(AuthResponse.Loading)
        try {
            val response = auth.createUserWithEmailAndPassword(email, password).await()
            val currentUser = response.user
            currentUser?.let { user ->
                emit(AuthResponse.Success(user))
            } ?: run { emit(AuthResponse.Error("Authentication Failed!!")) }
        } catch (exception : Exception) {
            emit(AuthResponse.Error(exception.localizedMessage ?: "Unknown Error!!"))
        }
    }.flowOn(Dispatchers.IO)
}