package com.example.letterboxie.data.remote

import com.example.letterboxie.models.alternativeTitles.AlternativeTitlesResponse
import com.example.letterboxie.models.credits.MovieCreditsResponse
import com.example.letterboxie.models.details.MovieDetailsResponse
import com.example.letterboxie.models.nowPlaying.NowPlayingResponse
import com.example.letterboxie.models.popular.PopularResponse
import com.example.letterboxie.models.related.RelatedResponse
import com.example.letterboxie.models.review.ReviewResponse
import com.example.letterboxie.models.search.SearchResponse
import com.example.letterboxie.models.similar.SimilarResponse
import com.example.letterboxie.models.topRated.TopRatedResponse
import com.example.letterboxie.models.upcoming.UpcomingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET("movie/popular")
    suspend fun getPopularMovies() : Response < PopularResponse >
    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies() : Response < NowPlayingResponse >
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies() : Response < UpcomingResponse >
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies() : Response < TopRatedResponse >
    @GET("movie/{movie_ID}/reviews")
    suspend fun getMovieReviews(@Path("movie_ID") movieID : Int) : Response < ReviewResponse >
    @GET("movie/{movie_ID}")
    suspend fun getMovieDetails(@Path("movie_ID") movieID : Int) : Response < MovieDetailsResponse >
    @GET("movie/{movie_ID}/credits")
    suspend fun getMovieCredits(@Path("movie_ID") movieID : Int) : Response < MovieCreditsResponse >
    @GET("collection/{collection_ID}")
    suspend fun getRelatedMovies(@Path("collection_ID") collectionID : Int) : Response < RelatedResponse >
    @GET("movie/{movie_ID}/similar")
    suspend fun getSimilarMovies(@Path("movie_ID") movieID : Int) : Response < SimilarResponse >
    @GET("movie/{movie_ID}/alternative_titles")
    suspend fun getAlternativeTitles(@Path("movie_ID") movieID : Int) : Response < AlternativeTitlesResponse >
    @GET("search/movie")
    suspend fun search(@Query("query") query : String) : Response < SearchResponse >
}