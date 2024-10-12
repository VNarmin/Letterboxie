package com.example.letterboxie.userInterface.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.databinding.CardPrimaryActionsBinding
import com.example.letterboxie.models.firestore.MoviePrimaryActions

class MoviePrimaryActionsAdapter : RecyclerView.Adapter < MoviePrimaryActionsAdapter.MoviePrimaryActionsViewHolder > () {
    private val movies = arrayListOf < MoviePrimaryActions > ()
    lateinit var onClick : (Int) -> Unit

    inner class MoviePrimaryActionsViewHolder(val binding : CardPrimaryActionsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MoviePrimaryActionsViewHolder {
        val view = CardPrimaryActionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoviePrimaryActionsViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder : MoviePrimaryActionsViewHolder, position : Int) {
        val movie = movies[position]
        holder.binding.moviePrimaryActions = movie
        holder.binding.cardViewMovie.setOnClickListener { onClick(movie.movieCore.movieID) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(data : List < MoviePrimaryActions >) {
        movies.clear()
        movies.addAll(data)
        notifyDataSetChanged()
    }
}