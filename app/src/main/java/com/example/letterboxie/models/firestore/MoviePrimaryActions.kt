package com.example.letterboxie.models.firestore

import com.example.letterboxie.models.base.MovieCore
import com.google.firebase.firestore.DocumentId

data class MoviePrimaryActions(
    @DocumentId val documentID : String = "",
    val favorite : Boolean = false,
    val addedToFavoritesAt : String = "",
    val alreadyWatched : Boolean = false,
    val addedToAlreadyWatchedAt : String = "",
    val liked : Boolean = false,
    val watchLater : Boolean = false,
    val addedToWatchLaterAt : String = "",
    val userRating : Float = 0f,
    val movieCore : MovieCore = MovieCore()
)
