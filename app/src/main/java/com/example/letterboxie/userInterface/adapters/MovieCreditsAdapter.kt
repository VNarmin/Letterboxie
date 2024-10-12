package com.example.letterboxie.userInterface.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.databinding.CardMovieCreditsBinding
import com.example.letterboxie.models.base.MovieCredits

class MovieCreditsAdapter : RecyclerView.Adapter < MovieCreditsAdapter.MovieCreditsViewHolder > () {
    private val movieCredits = arrayListOf < MovieCredits > ()
    inner class MovieCreditsViewHolder(val binding : CardMovieCreditsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MovieCreditsViewHolder {
        val view = CardMovieCreditsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieCreditsViewHolder(view)
    }

    override fun getItemCount() = movieCredits.size

    override fun onBindViewHolder(holder : MovieCreditsViewHolder, position : Int) {
        val data = movieCredits[position]
        holder.binding.movieCredits = data
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovieCredits(data : List < MovieCredits >) {
        movieCredits.clear()
        movieCredits.addAll(data)
        notifyDataSetChanged()
    }
}