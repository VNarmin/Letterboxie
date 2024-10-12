package com.example.letterboxie.userInterface.detailScreens.cast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentCastBinding
import com.example.letterboxie.userInterface.adapters.MovieCreditsAdapter
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CastFragment : BaseFragment < FragmentCastBinding > (FragmentCastBinding::inflate) {
    private val castViewModel by activityViewModels < CastViewModel > ()
    private val adapterCast = MovieCreditsAdapter()
    private var movieID : Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCast.adapter = adapterCast
        movieID = arguments?.getInt("movieID")
        observeData()
        castViewModel.fetchMovieDetails(movieID!!)
    }

    private fun observeData() {
        castViewModel.castUIState.observe(viewLifecycleOwner) { castUIState ->
            castUIState?.let { state -> when (state) {
                    is MovieCreditsUIState.Success -> {
                        adapterCast.updateMovieCredits(state.movieCredits)
                        binding.root.requestLayout()
                    }
                    is MovieCreditsUIState.Error -> { requireContext().showToastMessage(state.errorMessage) }
                    is MovieCreditsUIState.Loading -> { }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        with(castViewModel) {
            castUIState.value = null
            crewUIState.value = null
            movieDirector.value = null
            movieDetailsUIState.value = null
            alternativeTitlesUIState.value = null
        }
    }
}