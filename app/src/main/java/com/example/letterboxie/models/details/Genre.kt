package com.example.letterboxie.models.details

import com.example.letterboxie.models.base.MovieInfo
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    val id : Int?,
    @SerializedName("name")
    override val name : String?
) : MovieInfo()