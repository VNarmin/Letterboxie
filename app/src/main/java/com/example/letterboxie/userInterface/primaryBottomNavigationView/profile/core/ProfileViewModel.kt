package com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.models.firestore.MoviePrimaryActions
import com.example.letterboxie.models.firestore.UserProfile
import com.example.letterboxie.models.firestore.UserReview
import com.example.letterboxie.repository.FirestoreRepository
import com.example.letterboxie.repository.NetworkResponse
import com.example.letterboxie.utilities.Constants.MOVIES
import com.example.letterboxie.utilities.Constants.REVIEWS
import com.example.letterboxie.utilities.Constants.USER_PROFILE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val firestoreRepository : FirestoreRepository,
    private val sp : SharedPreferences) : ViewModel() {
    val retrieveMovies = MutableLiveData < ProfileUIState < MoviePrimaryActions >> ()
    val retrieveReviews = MutableLiveData < ProfileUIState < UserReview >> ()
    val username = MutableLiveData < String > ()

    init {
        retrieveUsername()
        retrieveMovieCollection()
        retrieveReviewCollection()
    }

    fun retrieveMovieCollection() {
        viewModelScope.launch {
            val currentUserID = retrieveCurrentUserID()
            currentUserID?.let { userID ->
                firestoreRepository.retrieveCollection(userDocumentID = userID, collectionPath = MOVIES,
                    clazz = MoviePrimaryActions::class.java).collectLatest { response ->
                    retrieveMovies.value = when (response) {
                        is NetworkResponse.Success -> ProfileUIState.Success(response.data)
                        is NetworkResponse.Error -> ProfileUIState.Error(response.errorMessage)
                        is NetworkResponse.Loading -> ProfileUIState.Loading()
                    }
                }
            }
        }
    }

    fun retrieveReviewCollection() {
        viewModelScope.launch {
            val currentUserID = retrieveCurrentUserID()
            currentUserID?.let { userID ->
                firestoreRepository.retrieveCollection(userDocumentID = userID, collectionPath = REVIEWS,
                    clazz = UserReview::class.java).collectLatest { response ->
                        retrieveReviews.value = when (response) {
                            is NetworkResponse.Success -> ProfileUIState.Success(response.data)
                            is NetworkResponse.Error -> ProfileUIState.Error(response.errorMessage)
                            is NetworkResponse.Loading -> ProfileUIState.Loading()
                        }
                }
            }
        }
    }

    private fun retrieveUsername() {
        viewModelScope.launch {
            val currentUserID = retrieveCurrentUserID()
            currentUserID?.let { userID ->
                firestoreRepository.retrieveDocument(userDocumentID = userID, collectionPath = USER_PROFILE,
                    childDocumentID = userID, clazz = UserProfile::class.java).collectLatest { response ->
                        username.value = when (response) {
                            is NetworkResponse.Success -> response.data.username
                            is NetworkResponse.Error -> ""
                            is NetworkResponse.Loading -> ""
                        }
                }
            }
        }
    }

    private fun retrieveCurrentUserID() : String? {
        return sp.getString("current_user_ID", null)
    }

    fun resetRememberMe() {
        val editor = sp.edit()
        editor.putBoolean("remember_me", false)
        editor.apply()
    }
}

sealed class ProfileUIState <T> {
    data class Success <T> (val collection : List < T >) : ProfileUIState <T> ()
    data class Error <T> (val errorMessage : String) : ProfileUIState <T> ()
    class Loading <T> : ProfileUIState <T> ()
}