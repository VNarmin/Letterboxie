package com.example.letterboxie.userInterface.homeBottomNavigationView.reviews

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentReviewsBinding
import com.example.letterboxie.models.review.ReviewExtended
import com.example.letterboxie.userInterface.adapters.ReviewAdapter
import com.example.letterboxie.userInterface.homeBottomNavigationView.popular.PopularViewModel
import com.example.letterboxie.userInterface.homeBottomNavigationView.popular.ReviewsUIState
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : BaseFragment < FragmentReviewsBinding > (FragmentReviewsBinding::inflate) {
    private val popularViewModel by activityViewModels < PopularViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()
    private val reviewAdapter = ReviewAdapter()
    private val reviewsExtended = arrayListOf < ReviewExtended > ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewReviews.adapter = reviewAdapter
        observeData()
    }

    private fun observeData() {
        popularViewModel.reviewsUIStates.forEach { reviewsUIState ->
            reviewsUIState.observe(viewLifecycleOwner) { state -> manageRequestStates(state) }
        }
    }

    private fun manageRequestStates(state : ReviewsUIState) {
        when (state) {
            is ReviewsUIState.Success -> {
                val reviewsFiltered = state.reviews.filterNot { review -> review in reviewsExtended }
                reviewsExtended.addAll(reviewsFiltered)
                reviewAdapter.updateReviews(reviewsExtended)
                sharedViewModel.setProgressAnimationVisibility(false)
            }
            is ReviewsUIState.Error -> {
                requireContext().showToastMessage(state.errorMessage)
                sharedViewModel.setProgressAnimationVisibility(false)
            }
            is ReviewsUIState.Loading -> sharedViewModel.setProgressAnimationVisibility(true)
        }
    }
}