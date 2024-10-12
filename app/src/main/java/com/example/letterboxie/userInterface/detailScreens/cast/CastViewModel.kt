package com.example.letterboxie.userInterface.detailScreens.cast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.models.base.MovieCredits
import com.example.letterboxie.models.base.MovieInfo
import com.example.letterboxie.models.credits.Crew
import com.example.letterboxie.repository.LetterboxieRepository
import com.example.letterboxie.repository.NetworkResponse
import com.example.letterboxie.userInterface.detailScreens.core.MovieDetailsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CastViewModel @Inject constructor(private val repository : LetterboxieRepository) : ViewModel() {
    val castUIState = MutableLiveData < MovieCreditsUIState? > ()
    val crewUIState = MutableLiveData < MovieCreditsUIState? > ()
    val movieDirector = MutableLiveData < String? > ()
    val alternativeTitlesUIState = MutableLiveData < AlternativeTitlesUIState? > ()
    val movieDetailsUIState = MutableLiveData < MovieDetailsUIState? > ()

    fun fetchMovieDetails(movieID : Int) {
        getMovieCredits(movieID)
        getMovieDetails(movieID)
        getAlternativeTitles(movieID)
    }

    private fun getMovieCredits(movieID : Int) {
        viewModelScope.launch {
            repository.getMovieCredits(movieID).collectLatest { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.cast.isNullOrEmpty()) {   // for retrieving the movie cast
                            castUIState.value = MovieCreditsUIState.Error("Null or Empty!!")
                        } else {
                            castUIState.value = MovieCreditsUIState.Success(response.data.cast)
                        }
                        if (response.data.crew.isNullOrEmpty()) {   // for retrieving the movie crew
                            crewUIState.value = MovieCreditsUIState.Error("Null or Empty!!")
                        } else {
                            crewUIState.value = MovieCreditsUIState.Success(response.data.crew)
                            saveMovieDirector(response.data.crew)
                        }
                    }
                    is NetworkResponse.Error -> {
                        castUIState.value = MovieCreditsUIState.Error(response.errorMessage)
                        crewUIState.value = MovieCreditsUIState.Error(response.errorMessage)
                    }
                    is NetworkResponse.Loading -> {
                        castUIState.value = MovieCreditsUIState.Loading
                        crewUIState.value = MovieCreditsUIState.Loading
                    }
                }
            }
        }
    }

    private fun saveMovieDirector(crew : List < Crew >) {
        val director = crew.find { member -> member.character == "Director" }
        director?.let { mDirector -> movieDirector.value = mDirector.name }
    }

    private fun getMovieDetails(movieID : Int) {
        viewModelScope.launch {
            repository.getMovieDetails(movieID).collectLatest { response ->
                movieDetailsUIState.value = when (response) {
                    is NetworkResponse.Success -> MovieDetailsUIState.Success(response.data)
                    is NetworkResponse.Error -> MovieDetailsUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> MovieDetailsUIState.Loading
                }
            }
        }
    }

    private fun getAlternativeTitles(movieID : Int) {
        viewModelScope.launch {
            repository.getAlternativeTitles(movieID).collectLatest { response ->
                alternativeTitlesUIState.value = when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.alternativeTitles.isNullOrEmpty()) {
                            AlternativeTitlesUIState.Error("Null or Empty!!")
                        } else AlternativeTitlesUIState.Success(response.data.alternativeTitles)
                    }
                    is NetworkResponse.Error -> AlternativeTitlesUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> AlternativeTitlesUIState.Loading
                }
            }
        }
    }
}

sealed class MovieCreditsUIState {
    data class Success(val movieCredits : List < MovieCredits >) : MovieCreditsUIState()
    data class Error(val errorMessage : String) : MovieCreditsUIState()
    data object Loading : MovieCreditsUIState()
}

sealed class AlternativeTitlesUIState {
    data class Success(val alternativeTitles : List < MovieInfo >) : AlternativeTitlesUIState()
    data class Error(val errorMessage : String) : AlternativeTitlesUIState()
    data object Loading : AlternativeTitlesUIState()
}