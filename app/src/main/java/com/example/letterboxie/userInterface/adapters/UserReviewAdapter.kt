package com.example.letterboxie.userInterface.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.databinding.CardUserReviewBinding
import com.example.letterboxie.models.firestore.UserReview

class UserReviewAdapter : RecyclerView.Adapter < UserReviewAdapter.UserReviewViewHolder > () {
    private val userReviews = arrayListOf < UserReview > ()
    lateinit var onDelete : (String) -> Unit

    inner class UserReviewViewHolder(val binding : CardUserReviewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : UserReviewViewHolder {
        val view = CardUserReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserReviewViewHolder(view)
    }

    override fun getItemCount() = userReviews.size

    override fun onBindViewHolder(holder : UserReviewViewHolder, position : Int) {
        val userReview = userReviews[position]
        holder.binding.userReview = userReview
    }

    fun deleteUserReview(position : Int) {
        val userReviewToBeRemoved = userReviews[position]
        userReviews.removeAt(position)
        onDelete(userReviewToBeRemoved.movieCore.movieID.toString())
        notifyItemRemoved(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateUserReviews(data : List < UserReview >) {
        userReviews.clear()
        userReviews.addAll(data)
        notifyDataSetChanged()
    }
}