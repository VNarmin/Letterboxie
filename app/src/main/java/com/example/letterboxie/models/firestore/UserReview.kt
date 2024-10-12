package com.example.letterboxie.models.firestore

import com.example.letterboxie.models.base.MovieCore
import com.google.firebase.firestore.DocumentId

data class UserReview(
    @DocumentId val documentID : String = "",
    val userRating : Float = 0f,
    val liked : Boolean = false,
    val reviewContent : String = "",
    val savedAt : String = "",
    val movieCore : MovieCore = MovieCore()
)