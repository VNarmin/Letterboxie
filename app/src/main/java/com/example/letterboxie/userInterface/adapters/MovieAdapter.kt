package com.example.letterboxie.userInterface.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.databinding.CardMovieBinding
import com.example.letterboxie.models.base.Movie

class MovieAdapter : RecyclerView.Adapter < MovieAdapter.MovieViewHolder > () {
    private val movies = arrayListOf < Movie > ()
    lateinit var onClick : (Int) -> Unit
    inner class MovieViewHolder(val binding : CardMovieBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MovieViewHolder {
        val view = CardMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder : MovieViewHolder, position : Int) {
        val movie = movies[position]
        holder.binding.movie = movie
        holder.binding.cardMovie.setOnClickListener {
            movie.id?.let { movieID -> onClick(movieID) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(data : List < Movie >) {
        movies.clear()
        movies.addAll(data)
        notifyDataSetChanged()
    }
}