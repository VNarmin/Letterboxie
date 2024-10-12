package com.example.letterboxie.userInterface.bottomSheets

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.models.firestore.MoviePrimaryActions
import com.example.letterboxie.models.firestore.UserReview
import com.example.letterboxie.repository.FirestoreRepository
import com.example.letterboxie.repository.NetworkResponse
import com.example.letterboxie.utilities.Constants.MOVIES
import com.example.letterboxie.utilities.Constants.REVIEWS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrimaryActionsBottomSheetViewModel @Inject constructor(private val firestoreRepository : FirestoreRepository,
    private val sp : SharedPreferences) : ViewModel() {
    val retrieveMovie = MutableLiveData < BottomSheetUIState > ()
    val errorSaveMovie = MutableLiveData < String > ()
    val errorSaveReview = MutableLiveData < String > ()
    val errorDeleteMovie = MutableLiveData < String > ()

    fun retrieveMovieDocument(childDocumentID : String) {
        viewModelScope.launch {
            val currentUserID = retrieveCurrentUserID()
            currentUserID?.let { userID ->
                firestoreRepository.retrieveDocument(userDocumentID = userID, collectionPath = MOVIES,
                    childDocumentID = childDocumentID,
                    clazz = MoviePrimaryActions::class.java).collectLatest { response ->
                    retrieveMovie.value = when (response) {
                        is NetworkResponse.Success -> BottomSheetUIState.Success(response.data)
                        is NetworkResponse.Error -> BottomSheetUIState.Error(response.errorMessage)
                        is NetworkResponse.Loading -> BottomSheetUIState.Loading
                    }
                }
            }
        }
    }

    fun saveMovieDocument(data : MoviePrimaryActions) {
        viewModelScope.launch {
            val currentUserID = retrieveCurrentUserID()
            currentUserID?.let { userID ->
                firestoreRepository.saveDocument(userDocumentID = userID, collectionPath = MOVIES,
                    childDocumentID = data.movieCore.movieID.toString(), data = data).collectLatest { response ->
                    when (response) {
                        is NetworkResponse.Success -> { }
                        is NetworkResponse.Error -> errorSaveMovie.value = response.errorMessage
                        is NetworkResponse.Loading -> { }
                    }
                }
            }
        }
    }

    fun saveReviewDocument(data : UserReview) {
        viewModelScope.launch {
            val currentUserID = retrieveCurrentUserID()
            currentUserID?.let { userID ->
                firestoreRepository.saveDocument(userDocumentID = userID, collectionPath = REVIEWS,
                    childDocumentID = data.movieCore.movieID.toString(), data = data).collectLatest { response ->
                    when (response) {
                        is NetworkResponse.Success -> { }
                        is NetworkResponse.Error -> errorSaveReview.value = response.errorMessage
                        is NetworkResponse.Loading -> { }
                    }
                }
            }
        }
    }

    fun deleteMovieDocument(childDocumentID : String) {
        viewModelScope.launch {
            val currentUserID = retrieveCurrentUserID()
            currentUserID?.let { userID ->
                firestoreRepository.deleteDocument(userDocumentID = userID, collectionPath = MOVIES,
                    childDocumentID = childDocumentID).collectLatest { response ->
                    when (response) {
                        is NetworkResponse.Success -> { }
                        is NetworkResponse.Error -> errorDeleteMovie.value = response.errorMessage
                        is NetworkResponse.Loading -> { }
                    }
                }
            }
        }
    }

    private fun retrieveCurrentUserID() : String? {
        return sp.getString("current_user_ID", null)
    }
}

sealed class BottomSheetUIState {
    data class Success(val documentData : MoviePrimaryActions) : BottomSheetUIState()
    data class Error(val errorMessage : String) : BottomSheetUIState ()
    data object Loading : BottomSheetUIState()
}