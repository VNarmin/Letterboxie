package com.example.letterboxie.repository

import com.example.letterboxie.data.remote.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class LetterboxieRepository @Inject constructor(private val service : Service) {
    fun getPopularMovies() = safeRequest { service.getPopularMovies() }
    fun getNowPlayingMovies() = safeRequest { service.getNowPlayingMovies() }
    fun getUpcomingMovies() = safeRequest { service.getUpcomingMovies() }
    fun getTopRatedMovies() = safeRequest { service.getTopRatedMovies() }
    fun getMovieReviews(movieID : Int) = safeRequest { service.getMovieReviews(movieID) }
    fun getMovieDetails(movieID : Int) = safeRequest { service.getMovieDetails(movieID) }
    fun getMovieCredits(movieID : Int) = safeRequest { service.getMovieCredits(movieID) }
    fun getRelatedMovies(collectionID : Int) = safeRequest { service.getRelatedMovies(collectionID) }
    fun getSimilarMovies(movieID : Int) = safeRequest { service.getSimilarMovies(movieID) }
    fun getAlternativeTitles(movieID : Int) = safeRequest { service.getAlternativeTitles(movieID) }
    fun search(query : String) = safeRequest { service.search(query) }

    private fun <T> safeRequest(get : suspend() -> Response < T > ) : Flow < NetworkResponse < T > > = flow {
        emit(NetworkResponse.Loading)
        try {
            val response = get.invoke()
            if (response.isSuccessful) {
                val responseBody = response.body()
                responseBody?.let { data ->
                    emit(NetworkResponse.Success(data))
                } ?: run { emit(NetworkResponse.Error("Null")) }
            }
            else emit(NetworkResponse.Error(response.errorBody().toString()))
        } catch (exception : Exception) {
            emit(NetworkResponse.Error(exception.localizedMessage ?: "Unknown Error!!"))
        }
    }.flowOn(Dispatchers.IO)
}