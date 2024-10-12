package com.example.letterboxie.models.details

import com.example.letterboxie.models.base.MovieInfo
import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    @SerializedName("id")
    val id : Int?,
    @SerializedName("logo_path")
    val logoPath : String?,
    @SerializedName("name")
    override val name : String?,
    @SerializedName("origin_country")
    val originCountry : String?
) : MovieInfo()