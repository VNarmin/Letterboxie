package com.example.letterboxie.models.details

import com.example.letterboxie.models.base.MovieInfo
import com.google.gson.annotations.SerializedName

data class Language(
    @SerializedName("english_name")
    val englishName : String?,
    @SerializedName("iso_639_1")
    val iso6391 : String?,
    @SerializedName("name")
    override val name : String?
) : MovieInfo()