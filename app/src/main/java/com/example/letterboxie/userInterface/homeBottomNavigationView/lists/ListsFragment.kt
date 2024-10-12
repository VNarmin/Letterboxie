package com.example.letterboxie.userInterface.homeBottomNavigationView.lists

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.userInterface.adapters.MovieAdapter
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentListsBinding
import com.example.letterboxie.userInterface.homeBottomNavigationView.popular.MovieListsUIState
import com.example.letterboxie.userInterface.homeBottomNavigationView.popular.PopularViewModel
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.utilities.showToastMessage
import com.example.letterboxie.utilities.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListsFragment : BaseFragment < FragmentListsBinding > (FragmentListsBinding::inflate) {
    private val popularViewModel by activityViewModels < PopularViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()
    private val adapterNowPlaying = MovieAdapter()
    private val adapterUpcoming = MovieAdapter()
    private val adapterTopRated = MovieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewNowPlaying.adapter = adapterNowPlaying
        binding.recyclerViewUpcoming.adapter = adapterUpcoming
        binding.recyclerViewTopRated.adapter = adapterTopRated
        observeData()
        adapterNowPlaying.onClick = { movieID -> sharedViewModel.setMovieID(movieID) }
        adapterUpcoming.onClick = { movieID -> sharedViewModel.setMovieID(movieID) }
        adapterTopRated.onClick = { movieID -> sharedViewModel.setMovieID(movieID) }
    }

    private fun observeData() {
        popularViewModel.nowPlayingUIState.observe(viewLifecycleOwner) { state ->
            manageRequestStates(state = state, textView = binding.textViewNowPlaying,
                recyclerView = binding.recyclerViewNowPlaying, movieAdapter = adapterNowPlaying)
        }
        popularViewModel.upcomingUIState.observe(viewLifecycleOwner) { state ->
           manageRequestStates(state = state, textView = binding.textViewUpcoming,
               recyclerView = binding.recyclerViewUpcoming, movieAdapter = adapterUpcoming)
        }
        popularViewModel.topRatedUIState.observe(viewLifecycleOwner) { state ->
            manageRequestStates(state = state, textView = binding.textViewTopRated,
                recyclerView = binding.recyclerViewTopRated, movieAdapter = adapterTopRated)
        }
    }

    private fun manageRequestStates(state : MovieListsUIState, textView : TextView, recyclerView : RecyclerView,
                                    movieAdapter : MovieAdapter) {
        when (state) {
            is MovieListsUIState.Success -> {
                textView.visible()
                recyclerView.visible()
                movieAdapter.updateMovies(state.movies)
                sharedViewModel.setProgressAnimationVisibility(false)
            }
            is MovieListsUIState.Error -> {
                requireContext().showToastMessage(state.errorMessage)
                sharedViewModel.setProgressAnimationVisibility(false)
            }
            is MovieListsUIState.Loading -> sharedViewModel.setProgressAnimationVisibility(true)
        }
    }
}