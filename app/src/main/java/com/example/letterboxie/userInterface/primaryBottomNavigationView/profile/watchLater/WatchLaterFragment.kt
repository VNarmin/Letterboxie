package com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.watchLater

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentWatchLaterBinding
import com.example.letterboxie.userInterface.adapters.WatchLaterAdapter
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core.ProfileUIState
import com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core.ProfileViewModel
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class WatchLaterFragment : BaseFragment < FragmentWatchLaterBinding > (FragmentWatchLaterBinding::inflate) {
    private val profileViewModel by activityViewModels < ProfileViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()
    private val adapterWatchLater = WatchLaterAdapter()
    @Inject
    lateinit var formatter : SimpleDateFormat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewWatchLater.adapter = adapterWatchLater
        observeData()
        binding.imageButtonWatchLaterBack.setOnClickListener { parentFragmentManager.popBackStack() }
        adapterWatchLater.onClick = { movieID ->
            findNavController().navigate(WatchLaterFragmentDirections.actionWatchLaterFragmentToMovieDetailsFragment(movieID))
        }
    }

    private fun observeData() {
        profileViewModel.retrieveMovies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileUIState.Success -> {
                    val watchLater = state.collection.filter { movie -> movie.watchLater }
                        .sortedByDescending { movie -> formatter.parse(movie.addedToWatchLaterAt) }
                    adapterWatchLater.updateMovies(watchLater)
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