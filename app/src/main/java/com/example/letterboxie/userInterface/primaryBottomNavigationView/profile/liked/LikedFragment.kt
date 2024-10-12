package com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.liked

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentLikedBinding
import com.example.letterboxie.userInterface.adapters.MoviePrimaryActionsAdapter
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core.ProfileUIState
import com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core.ProfileViewModel
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LikedFragment : BaseFragment < FragmentLikedBinding > (FragmentLikedBinding::inflate) {
    private val profileViewModel by activityViewModels < ProfileViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()
    private val adapterLiked = MoviePrimaryActionsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewLikes.adapter = adapterLiked
        observeData()
        binding.imageButtonLikesBack.setOnClickListener { parentFragmentManager.popBackStack() }
        adapterLiked.onClick = { movieID ->
            findNavController().navigate(LikedFragmentDirections.actionLikedFragmentToMovieDetailsFragment(movieID))
        }
    }

    private fun observeData() {
        profileViewModel.retrieveMovies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileUIState.Success -> {
                    val liked = state.collection.filter { movie -> movie.liked }
                    val sorted = liked.sortedByDescending { movie -> movie.movieCore.movieReleaseDate.take(4).toInt() }
                    adapterLiked.updateMovies(sorted)
                    sharedViewModel.setProgressAnimationVisibility(false)
                }
                is ProfileUIState.Error -> {
                    requireContext().showToastMessage(state.errorMessage)
                    sharedViewModel.setProgressAnimationVisibility(false)
                }
                is ProfileUIState.Loading -> sharedViewModel.setProgressAnimationVisibility(true)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.retrieveMovieCollection()
    }
}