package com.example.letterboxie.models.related

import com.google.gson.annotations.SerializedName

data class RelatedResponse(
    @SerializedName("backdrop_path")
    val backdropPath : String?,
    @SerializedName("id")
    val id : Int?,
    @SerializedName("name")
    val name : String?,
    @SerializedName("overview")
    val overview : String?,
    @SerializedName("parts")
    val relatedMovies : List < RelatedMovie > ?,
    @SerializedName("poster_path")
    val posterPath : String?
)