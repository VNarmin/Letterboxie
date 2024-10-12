package com.example.letterboxie.models.alternativeTitles

import com.google.gson.annotations.SerializedName

data class AlternativeTitlesResponse(
    @SerializedName("id")
    val id : Int?,
    @SerializedName("titles")
    val alternativeTitles : List < AlternativeTitle > ?
)