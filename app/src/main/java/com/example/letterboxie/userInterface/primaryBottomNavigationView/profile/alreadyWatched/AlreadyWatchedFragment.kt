package com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.alreadyWatched

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentAlreadyWatchedBinding
import com.example.letterboxie.userInterface.adapters.MoviePrimaryActionsAdapter
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core.ProfileUIState
import com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core.ProfileViewModel
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlreadyWatchedFragment : BaseFragment < FragmentAlreadyWatchedBinding > (FragmentAlreadyWatchedBinding::inflate) {
    private val profileViewModel by activityViewModels < ProfileViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()
    private val adapterAlreadyWatched = MoviePrimaryActionsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewAlreadyWatched.adapter = adapterAlreadyWatched
        observeData()
        binding.imageButtonMoviesBack.setOnClickListener { parentFragmentManager.popBackStack() }
        adapterAlreadyWatched.onClick = { movieID ->
            findNavController().navigate(AlreadyWatchedFragmentDirections.
            actionAlreadyWatchedFragmentToMovieDetailsFragment(movieID))
        }
    }

    private fun observeData() {
        profileViewModel.retrieveMovies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileUIState.Success -> {
                    val alreadyWatched = state.collection.filter { movie -> movie.alreadyWatched }
                    val sorted = alreadyWatched.sortedByDescending { movie -> movie.movieCore.movieReleaseDate.take(4).toInt() }
                    adapterAlreadyWatched.updateMovies(sorted)
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