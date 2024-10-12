package com.example.letterboxie.models.details

import com.example.letterboxie.models.base.MovieInfo
import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    val iso31661 : String?,
    @SerializedName("name")
    override val name : String?
) : MovieInfo()