package com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.userReviews

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.repository.FirestoreRepository
import com.example.letterboxie.repository.NetworkResponse
import com.example.letterboxie.utilities.Constants.REVIEWS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserReviewsViewModel @Inject constructor(private val firestoreRepository : FirestoreRepository,
    private val sp : SharedPreferences) : ViewModel() {
    val errorMessage = MutableLiveData < String > ()

    fun deleteUserReview(childDocumentID : String) {
        viewModelScope.launch {
            val currentUserID = retrieveCurrentUserID()
            currentUserID?.let { userID ->
                firestoreRepository.deleteDocument(userDocumentID = userID, collectionPath = REVIEWS,
                    childDocumentID = childDocumentID).collectLatest { response ->
                    when (response) {
                        is NetworkResponse.Success -> { }
                        is NetworkResponse.Error -> errorMessage.value = response.errorMessage
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