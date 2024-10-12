package com.example.letterboxie.models.topRated

import com.google.gson.annotations.SerializedName

data class TopRatedResponse(
    @SerializedName("page")
    val page : Int?,
    @SerializedName("results")
    val topRatedMovies : List < TopRatedMovie > ?,
    @SerializedName("total_pages")
    val totalPages : Int?,
    @SerializedName("total_results")
    val totalResults : Int?
)