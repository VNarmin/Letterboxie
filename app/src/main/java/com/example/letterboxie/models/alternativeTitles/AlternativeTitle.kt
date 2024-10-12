package com.example.letterboxie.models.alternativeTitles

import com.example.letterboxie.models.base.MovieInfo
import com.google.gson.annotations.SerializedName

data class AlternativeTitle(
    @SerializedName("iso_3166_1")
    val iso31661 : String?,
    @SerializedName("title")
    override val name : String?,
    @SerializedName("type")
    val type : String?
) : MovieInfo()