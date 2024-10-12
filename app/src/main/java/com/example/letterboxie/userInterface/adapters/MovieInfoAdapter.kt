package com.example.letterboxie.userInterface.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.databinding.CardMovieInfoBinding
import com.example.letterboxie.models.base.MovieInfo

class MovieInfoAdapter : RecyclerView.Adapter < MovieInfoAdapter.MovieInfoViewHolder > () {
    private val movieInfo = arrayListOf < MovieInfo > ()
    inner class MovieInfoViewHolder(val binding : CardMovieInfoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MovieInfoViewHolder {
        val view = CardMovieInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieInfoViewHolder(view)
    }

    override fun getItemCount() = movieInfo.size

    override fun onBindViewHolder(holder : MovieInfoViewHolder, position : Int) {
        val info = movieInfo[position]
        holder.binding.movieInfo = info
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovieInfo(data : List < MovieInfo >) {
        movieInfo.clear()
        movieInfo.addAll(data)
        notifyDataSetChanged()
    }
}