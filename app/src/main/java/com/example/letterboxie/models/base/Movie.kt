package com.example.letterboxie.models.base

abstract class Movie {
    abstract val posterPath : String?
    abstract val id : Int?
    abstract val title : String?
    abstract val releaseDate : String?
    abstract val voteAverage : Double?
}