package com.example.letterboxie.models.nowPlaying

import com.google.gson.annotations.SerializedName

data class NowPlayingResponse(
    @SerializedName("dates")
    val dates : NowPlayingDates?,
    @SerializedName("page")
    val page : Int?,
    @SerializedName("results")
    val nowPlayingMovies : List < NowPlayingMovie > ?,
    @SerializedName("total_pages")
    val totalPages : Int?,
    @SerializedName("total_results")
    val totalResults : Int?
)