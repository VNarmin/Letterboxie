package com.example.letterboxie.utilities

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.letterboxie.R
import com.example.letterboxie.utilities.Constants.BACKDROP_BASE_URL
import com.example.letterboxie.utilities.Constants.POSTERS_BASE_URL
import com.example.letterboxie.utilities.Constants.PROFILE_BASE_URL

@BindingAdapter("set_movie_posters")
fun setMovieImages(imageView : ImageView, imageURL : String?) {
    val fullURL = imageURL?.let { url -> "${POSTERS_BASE_URL}$url" }
    imageView.setImage(fullURL, R.drawable.movie_icon, R.drawable.movie_icon)
}

@BindingAdapter("set_profiles")
fun setProfiles(imageView : ImageView, imageURL : String?) {
    val fullURL = imageURL?.let { url -> "${PROFILE_BASE_URL}$url" }
    imageView.setImage(fullURL, R.drawable.account_circle, R.drawable.account_circle)
}

@BindingAdapter("set_movie_backdrops")
fun setMovieBackdrops(imageView : ImageView, imageURL : String?) {
    val fullURL = imageURL?.let { url -> "${BACKDROP_BASE_URL}$url" }
    imageView.setImage(fullURL, R.drawable.movie_icon, R.drawable.movie_icon)
}

@BindingAdapter("set_runtime")
fun setMovieRuntime(textView : TextView, movieRuntime : Int?) {
    movieRuntime?.let { runtime ->
        textView.text = String.format("%d mins", runtime)
    } ?: run { textView.text = "" }
}

@BindingAdapter("set_average_rating")
fun setAverageRating(textView : TextView, movieAverageRating : Double?) {
    movieAverageRating?.let { averageRating ->
        textView.text = String.format("%.1f", averageRating / 2)
    } ?: run { textView.text = "" }
}

@BindingAdapter("set_whole_stars")
fun setWholeStars(ratingBar : RatingBar, authorRating : Double?) {
    authorRating?.let { rating ->
        val numStars = (rating / 2.0).toInt()
        if (numStars == 0) ratingBar.visibility = View.INVISIBLE
        else {
            ratingBar.numStars = numStars
            ratingBar.rating = numStars.toFloat()
        }
    } ?: run {
        ratingBar.numStars = 5
        ratingBar.rating = 0f
    }
}

@BindingAdapter("set_fractional_rating")
fun setFractionalRating(textView : TextView, authorRating : Double?) {
    authorRating?.let { rating ->
        val fractional = rating.toInt() % 2 != 0
        if (fractional) textView.text = "½" else textView.text = ""
    } ?: run { textView.text = "" }
}

@BindingAdapter("manage_like_visibility")
fun manageLikeVisibility(imageView : ImageView, liked : Boolean) {
    if (liked) imageView.visible() else imageView.gone()
}