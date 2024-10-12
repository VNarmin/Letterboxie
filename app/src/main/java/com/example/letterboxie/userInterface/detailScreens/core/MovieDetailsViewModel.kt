package com.example.letterboxie.userInterface.detailScreens.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.models.base.Movie
import com.example.letterboxie.models.base.MovieCore
import com.example.letterboxie.models.details.MovieDetailsResponse
import com.example.letterboxie.repository.LetterboxieRepository
import com.example.letterboxie.repository.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(private val repository : LetterboxieRepository) : ViewModel() {
    val movieDetailsUIState = MutableLiveData < MovieDetailsUIState > ()
    val relatedUIState = MutableLiveData < RelatedSimilarUIState > ()
    val similarUIState = MutableLiveData < RelatedSimilarUIState > ()
    val collectionID = MutableLiveData < Int? > ()
    val movieCore = MutableLiveData < MovieCore > ()

    fun fetchMovieDetails(movieID : Int) {
        getMovieDetails(movieID)
        getSimilarMovies(movieID)
    }

    private fun getMovieDetails(movieID : Int) {
        viewModelScope.launch {
            repository.getMovieDetails(movieID).collectLatest { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        movieDetailsUIState.value = MovieDetailsUIState.Success(response.data)
                        handleMapping(response.data)
                        response.data.collection?.id?.let { cID -> collectionID.value = cID}
                    }
                    is NetworkResponse.Error -> {
                        movieDetailsUIState.value = MovieDetailsUIState.Error(response.errorMessage)
                    }
                    is NetworkResponse.Loading -> movieDetailsUIState.value = MovieDetailsUIState.Loading
                }
            }
        }
    }

    fun getRelatedMovies(collectionID : Int) {
        viewModelScope.launch {
            repository.getRelatedMovies(collectionID).collectLatest { response ->
                relatedUIState.value = when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.relatedMovies.isNullOrEmpty()) {
                            RelatedSimilarUIState.Error("Null or Empty!!")
                        } else RelatedSimilarUIState.Success(response.data.relatedMovies)
                    }
                    is NetworkResponse.Error -> RelatedSimilarUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> RelatedSimilarUIState.Loading
                }
            }
        }
    }

    private fun getSimilarMovies(movieID : Int) {
        viewModelScope.launch {
            repository.getSimilarMovies(movieID).collectLatest { response ->
                similarUIState.value = when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.similarMovies.isNullOrEmpty()) {
                            RelatedSimilarUIState.Error("Null or Empty!!")
                        } else RelatedSimilarUIState.Success(response.data.similarMovies)
                    }
                    is NetworkResponse.Error -> RelatedSimilarUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> RelatedSimilarUIState.Loading
                }
            }
        }
    }

    private fun handleMapping(movieDetails : MovieDetailsResponse) {
        val core = MovieCore(
            movieID = movieDetails.id ?: 0,
            movieTitle = movieDetails.title ?: "",
            movieReleaseDate = movieDetails.releaseDate ?: "",
            moviePoster = movieDetails.posterPath ?: "")
        movieCore.value = core
    }
}

sealed class MovieDetailsUIState {
    data class Success(val movieDetails : MovieDetailsResponse) : MovieDetailsUIState()
    data class Error(val errorMessage : String) : MovieDetailsUIState()
    data object Loading : MovieDetailsUIState()
}

sealed class RelatedSimilarUIState {
    data class Success(val movies : List < Movie >) : RelatedSimilarUIState()
    data class Error(val errorMessage : String) : RelatedSimilarUIState()
    data object Loading : RelatedSimilarUIState()
}