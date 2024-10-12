package com.example.letterboxie.models.popular

import com.google.gson.annotations.SerializedName

data class PopularResponse(
    @SerializedName("page")
    val page : Int?,
    @SerializedName("results")
    val popularMovies : List < PopularMovie > ?,
    @SerializedName("total_pages")
    val totalPages : Int?,
    @SerializedName("total_results")
    val totalResults : Int?
)