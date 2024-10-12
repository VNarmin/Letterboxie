package com.example.letterboxie.userInterface.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.databinding.CardWatchLaterBinding
import com.example.letterboxie.models.firestore.MoviePrimaryActions

class WatchLaterAdapter : RecyclerView.Adapter < WatchLaterAdapter.WatchLaterViewHolder > () {
    private val movies = arrayListOf < MoviePrimaryActions > ()
    lateinit var onClick : (Int) -> Unit

    inner class WatchLaterViewHolder(val binding : CardWatchLaterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : WatchLaterViewHolder {
        val view = CardWatchLaterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchLaterViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder : WatchLaterViewHolder, position : Int) {
        val movie = movies[position]
        holder.binding.moviePrimaryActions = movie
        holder.binding.cardWatchLater.setOnClickListener { onClick(movie.movieCore.movieID) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(data : List < MoviePrimaryActions >) {
        movies.clear()
        movies.addAll(data)
        notifyDataSetChanged()
    }
}
