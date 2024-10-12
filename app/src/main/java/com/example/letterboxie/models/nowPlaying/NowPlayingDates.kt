package com.example.letterboxie.models.nowPlaying

import com.google.gson.annotations.SerializedName

data class NowPlayingDates(
    @SerializedName("maximum")
    val maximum : String?,
    @SerializedName("minimum")
    val minimum : String?
)