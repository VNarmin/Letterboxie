package com.example.letterboxie.userInterface.homeBottomNavigationView.popular

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.letterboxie.userInterface.adapters.MovieAdapter
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentPopularBinding
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment < FragmentPopularBinding > (FragmentPopularBinding::inflate) {
    private val popularViewModel by activityViewModels < PopularViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()
    private val movieAdapter = MovieAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewPopular.adapter = movieAdapter
        observeData()
        observeTopPopularMovies()
        movieAdapter.onClick = { movieID -> sharedViewModel.setMovieID(movieID) }
    }

    private fun observeData() {
        popularViewModel.popularUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MovieListsUIState.Success -> {
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

    private fun observeTopPopularMovies() {
        popularViewModel.topPopularMovies.observe(viewLifecycleOwner) { topPopularMovies ->
            topPopularMovies?.let { topPopMovies -> popularViewModel.fetchMovieReviews(topPopMovies) }
        }
    }
}