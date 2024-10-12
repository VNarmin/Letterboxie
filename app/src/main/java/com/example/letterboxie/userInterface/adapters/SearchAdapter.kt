package com.example.letterboxie.userInterface.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.databinding.CardSearchBinding
import com.example.letterboxie.models.base.Movie

class SearchAdapter : RecyclerView.Adapter < SearchAdapter.SearchViewHolder > () {
    private val movies = arrayListOf < Movie > ()
    lateinit var onClick : (Int) -> Unit
    inner class SearchViewHolder(val binding : CardSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : SearchViewHolder {
        val view = CardSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder : SearchViewHolder, position : Int) {
        val movie = movies[position]
        holder.binding.movie = movie
        holder.binding.cardSearchMovie.setOnClickListener {
            movie.id?.let{ movieID -> onClick(movieID) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(data : List < Movie >) {
        movies.clear()
        movies.addAll(data)
        notifyDataSetChanged()
    }
}