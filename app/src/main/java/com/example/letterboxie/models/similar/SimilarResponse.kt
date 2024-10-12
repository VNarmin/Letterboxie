package com.example.letterboxie.models.similar

import com.google.gson.annotations.SerializedName

data class SimilarResponse(
    @SerializedName("page")
    val page : Int?,
    @SerializedName("results")
    val similarMovies : List < SimilarMovie > ?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)