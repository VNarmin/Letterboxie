package com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.userReviews

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.letterboxie.R
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentUserReviewsBinding
import com.example.letterboxie.userInterface.adapters.UserReviewAdapter
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core.ProfileUIState
import com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core.ProfileViewModel
import com.example.letterboxie.utilities.SwipeGesture
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class UserReviewsFragment : BaseFragment < FragmentUserReviewsBinding > (FragmentUserReviewsBinding::inflate) {
    private val profileViewModel by activityViewModels < ProfileViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()
    private val userReviewsViewModel by viewModels < UserReviewsViewModel > ()
    private val adapterUserReview = UserReviewAdapter()
    @Inject
    lateinit var formatter : SimpleDateFormat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewUserReviews.adapter = adapterUserReview
        observeData()
        swipeToDelete()
        adapterUserReview.onDelete = { childDocumentID -> userReviewsViewModel.deleteUserReview(childDocumentID)}
        binding.imageButtonReviewsBack.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    private fun swipeToDelete() {
        val deleteBackground = ContextCompat.getColor(requireContext(), R.color.delete_background)
        val deleteIcon = R.drawable.delete
        val swipeGesture = SwipeGesture(deleteBackground, deleteIcon)
        swipeGesture.onSwipe = { position ->
            adapterUserReview.deleteUserReview(position)
        }
        val itemTouchHelper = ItemTouchHelper(swipeGesture)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewUserReviews)
    }

    private fun observeData() {
        profileViewModel.retrieveReviews.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileUIState.Success -> {
                    val reviews = state.collection.sortedByDescending { review -> formatter.parse(review.savedAt) }
                    adapterUserReview.updateUserReviews(reviews)
                    sharedViewModel.setProgressAnimationVisibility(false)
                }
                is ProfileUIState.Error -> {
                    requireContext().showToastMessage(state.errorMessage)
                    sharedViewModel.setProgressAnimationVisibility(false)
                }
                is ProfileUIState.Loading -> sharedViewModel.setProgressAnimationVisibility(true)
            }
        }
        userReviewsViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            requireContext().showToastMessage(errorMessage)
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.retrieveReviewCollection()
    }
}