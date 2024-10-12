package com.example.letterboxie.models.firestore

import com.google.firebase.firestore.DocumentId

data class UserProfile(
    @DocumentId val documentID : String = "",
    val username : String = "",
    val createdAt : String = ""
)
