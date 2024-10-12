package com.example.letterboxie.models.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieCore(
    val movieID : Int = 0,
    val movieTitle : String = "",
    val movieReleaseDate : String = "",
    val moviePoster : String = "") : Parcelable