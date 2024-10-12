package com.example.letterboxie.userInterface.primaryBottomNavigationView.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val loading = MutableLiveData < Boolean > ()
    val movieID = MutableLiveData < Int? > ()

    fun setProgressAnimationVisibility(visibility : Boolean) { loading.value = visibility }

    fun setMovieID(movieID : Int) { this.movieID.value = movieID }

    fun resetMovieID() { movieID.value = null }
}