package com.example.letterboxie.models.upcoming

import com.google.gson.annotations.SerializedName

data class UpcomingResponse(
    @SerializedName("dates")
    val dates : UpcomingDates?,
    @SerializedName("page")
    val page : Int?,
    @SerializedName("results")
    val upcomingMovies : List < UpcomingMovie > ?,
    @SerializedName("total_pages")
    val totalPages : Int?,
    @SerializedName("total_results")
    val totalResults : Int?
)