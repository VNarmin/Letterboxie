package com.example.letterboxie.userInterface.primaryBottomNavigationView.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.models.base.Movie
import com.example.letterboxie.repository.LetterboxieRepository
import com.example.letterboxie.repository.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(private val repository : LetterboxieRepository) : ViewModel() {
    val nowPlayingUIState = MutableLiveData < ExploreUIState > ()
    val exploreUIState = MutableLiveData < ExploreUIState > ()

    init { getNowPlayingMovies() }

    fun getNowPlayingMovies() {
        viewModelScope.launch {
            repository.getNowPlayingMovies().collectLatest { response ->
                nowPlayingUIState.value = when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.nowPlayingMovies.isNullOrEmpty()) {
                            ExploreUIState.Error("Null or Empty!!")
                        } else ExploreUIState.Success(response.data.nowPlayingMovies)
                    }
                    is NetworkResponse.Error -> ExploreUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> ExploreUIState.Loading
                }
            }
        }
    }

    fun search(query : String) {
        viewModelScope.launch {
            repository.search(query).collectLatest { response ->
                exploreUIState.value = when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.searchResults.isNullOrEmpty()) {
                            ExploreUIState.Error("Null or Empty!!")
                        } else ExploreUIState.Success(response.data.searchResults)
                    }
                    is NetworkResponse.Error -> ExploreUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> ExploreUIState.Loading
                }
            }
        }
    }
}

sealed class ExploreUIState {
    data class Success(val movies : List < Movie >) : ExploreUIState()
    data class Error(val errorMessage : String) : ExploreUIState()
    data object Loading : ExploreUIState()
}