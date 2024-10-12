package com.example.letterboxie.userInterface.homeBottomNavigationView.popular

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letterboxie.models.base.Movie
import com.example.letterboxie.models.base.MovieCore
import com.example.letterboxie.models.popular.PopularMovie
import com.example.letterboxie.models.review.ReviewExtended
import com.example.letterboxie.repository.LetterboxieRepository
import com.example.letterboxie.repository.NetworkResponse
import com.example.letterboxie.utilities.Constants.ENOUGH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(private val repository : LetterboxieRepository) : ViewModel() {
    val popularUIState = MutableLiveData < MovieListsUIState > ()
    val topPopularMovies = MutableLiveData < List < MovieCore >> ()
    val reviewsUIStates = List(ENOUGH) { MutableLiveData < ReviewsUIState > () }
    val nowPlayingUIState = MutableLiveData < MovieListsUIState > ()
    val upcomingUIState = MutableLiveData < MovieListsUIState > ()
    val topRatedUIState = MutableLiveData < MovieListsUIState > ()

    init {
        getPopularMovies()
        getNowPlayingMovies()
        getUpcomingMovies()
        getTopRatedMovies()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            repository.getPopularMovies().collectLatest { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.popularMovies.isNullOrEmpty()) {
                            popularUIState.value = MovieListsUIState.Error("Null or Empty!!")
                        } else {
                            popularUIState.value = MovieListsUIState.Success(response.data.popularMovies)
                            topPopularMovies.value = handleFilteringAndMapping(response.data.popularMovies)
                        }
                    }
                    is NetworkResponse.Error -> {
                        popularUIState.value = MovieListsUIState.Error(response.errorMessage)
                    }
                    is NetworkResponse.Loading -> popularUIState.value = MovieListsUIState.Loading
                }
            }
        }
    }

    private fun getNowPlayingMovies() {
        viewModelScope.launch {
            repository.getNowPlayingMovies().collectLatest { response ->
                nowPlayingUIState.value = when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.nowPlayingMovies.isNullOrEmpty()) {
                            MovieListsUIState.Error("Null or Empty!!")
                        } else MovieListsUIState.Success(response.data.nowPlayingMovies)
                    }
                    is NetworkResponse.Error -> MovieListsUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> MovieListsUIState.Loading
                }
            }
        }
    }

    private fun getUpcomingMovies() {
        viewModelScope.launch {
            repository.getUpcomingMovies().collectLatest { response ->
                upcomingUIState.value = when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.upcomingMovies.isNullOrEmpty()) {
                            MovieListsUIState.Error("Null or Empty!!")
                        } else MovieListsUIState.Success(response.data.upcomingMovies)
                    }
                    is NetworkResponse.Error -> MovieListsUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> MovieListsUIState.Loading
                }
            }
        }
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            repository.getTopRatedMovies().collectLatest { response ->
                topRatedUIState.value = when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.topRatedMovies.isNullOrEmpty()) {
                            MovieListsUIState.Error("Null or Empty!!")
                        } else MovieListsUIState.Success(response.data.topRatedMovies)
                    }
                    is NetworkResponse.Error -> MovieListsUIState.Error(response.errorMessage)
                    is NetworkResponse.Loading -> MovieListsUIState.Loading
                }
            }
        }
    }

    fun fetchMovieReviews(topPopularMovies : List < MovieCore >) {
        topPopularMovies.forEachIndexed { m, movieCore ->
            getMovieReviews(movieCore = movieCore, reviewsUIState = reviewsUIStates[m])
        }
    }

    private fun getMovieReviews(movieCore : MovieCore, reviewsUIState : MutableLiveData < ReviewsUIState >) {
        viewModelScope.launch {
            repository.getMovieReviews(movieCore.movieID).collectLatest { response ->
                when (response) {
                    is NetworkResponse.Success -> {
                        if (response.data.reviews.isNullOrEmpty()) {
                            reviewsUIState.value = ReviewsUIState.Error("Null or Empty!!")
                        } else {
                            val reviewsExtended = response.data.reviews.map { review ->
                                ReviewExtended(review = review, movieCore = movieCore)
                            }
                            reviewsUIState.value = ReviewsUIState.Success(reviewsExtended)
                        }
                    }
                    is NetworkResponse.Error -> {
                        reviewsUIState.value = ReviewsUIState.Error(response.errorMessage)
                    }
                    is NetworkResponse.Loading -> ReviewsUIState.Loading
                }
            }
        }
    }

    private fun handleFilteringAndMapping(popularMovies : List < PopularMovie >) : List < MovieCore > {
        return popularMovies
            .filter { movie ->
                movie.id != null && movie.title != null && movie.releaseDate != null && movie.posterPath != null }
            .take(ENOUGH)
            .map { movie ->
                MovieCore(
                    movieID = movie.id!!,
                    movieTitle = movie.title!!,
                    movieReleaseDate = movie.releaseDate!!,
                    moviePoster = movie.posterPath!!) }
    }
}

sealed class MovieListsUIState {
    data class Success(val movies : List < Movie > ) : MovieListsUIState()
    data class Error(val errorMessage : String) : MovieListsUIState()
    data object Loading : MovieListsUIState()
}

sealed class ReviewsUIState {
    data class Success(val reviews : List < ReviewExtended >) : ReviewsUIState()
    data class Error(val errorMessage : String) : ReviewsUIState()
    data object Loading : ReviewsUIState()
}