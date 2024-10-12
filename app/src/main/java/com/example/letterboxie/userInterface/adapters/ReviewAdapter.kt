package com.example.letterboxie.userInterface.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.databinding.CardReviewBinding
import com.example.letterboxie.models.review.ReviewExtended

class ReviewAdapter : RecyclerView.Adapter < ReviewAdapter.ReviewViewHolder > () {
    private val reviewsExtended = arrayListOf < ReviewExtended > ()
    inner class ReviewViewHolder(val binding : CardReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        init { binding.textViewReadMore.setOnClickListener { handleReadMoreReadLess(binding) } }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ReviewViewHolder {
        val view = CardReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(view)
    }

    override fun getItemCount() = reviewsExtended.size

    override fun onBindViewHolder(holder : ReviewViewHolder, position : Int) {
        val reviewExtended = reviewsExtended[position]
        holder.binding.reviewExtended = reviewExtended
    }

    private fun handleReadMoreReadLess(binding : CardReviewBinding) {
        val expanded = binding.textViewReviewContent.maxLines == Int.MAX_VALUE
        if (expanded) {
            binding.textViewReviewContent.maxLines = 5
            "Read More".also { more -> binding.textViewReadMore.text = more }
        } else {
            binding.textViewReviewContent.maxLines = Int.MAX_VALUE
            "Read Less".also { less -> binding.textViewReadMore.text = less }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateReviews(data : List < ReviewExtended >) {
        reviewsExtended.clear()
        reviewsExtended.addAll(data)
        notifyDataSetChanged()
    }
}