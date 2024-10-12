package com.example.letterboxie.models.nowPlaying

import com.example.letterboxie.models.base.Movie
import com.google.gson.annotations.SerializedName

data class NowPlayingMovie(
    @SerializedName("adult")
    val adult : Boolean?,
    @SerializedName("backdrop_path")
    val backdropPath : String?,
    @SerializedName("genre_ids")
    val genreIds : List < Int? > ?,
    @SerializedName("id")
    override val id : Int?,
    @SerializedName("original_language")
    val originalLanguage : String?,
    @SerializedName("original_title")
    val originalTitle : String?,
    @SerializedName("overview")
    val overview : String?,
    @SerializedName("popularity")
    val popularity : Double?,
    @SerializedName("poster_path")
    override val posterPath : String?,
    @SerializedName("release_date")
    override val releaseDate : String?,
    @SerializedName("title")
    override val title : String?,
    @SerializedName("video")
    val video : Boolean?,
    @SerializedName("vote_average")
    override val voteAverage : Double?,
    @SerializedName("vote_count")
    val voteCount : Int?
) : Movie()