package com.example.letterboxie.userInterface.detailScreens.crew

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentCrewBinding
import com.example.letterboxie.userInterface.adapters.MovieCreditsAdapter
import com.example.letterboxie.userInterface.detailScreens.cast.CastViewModel
import com.example.letterboxie.userInterface.detailScreens.cast.MovieCreditsUIState
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrewFragment : BaseFragment < FragmentCrewBinding > (FragmentCrewBinding::inflate) {
    private val castViewModel by activityViewModels < CastViewModel > ()
    private val adapterCrew = MovieCreditsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewCrew.adapter = adapterCrew
        observeData()
    }

    private fun observeData() {
        castViewModel.crewUIState.observe(viewLifecycleOwner) { crewUIState ->
            crewUIState?.let { state -> when (state) {
                    is MovieCreditsUIState.Success -> {
                        adapterCrew.updateMovieCredits(state.movieCredits)
                        binding.root.requestLayout()
                    }
                    is MovieCreditsUIState.Error -> requireContext().showToastMessage(state.errorMessage)
                    is MovieCreditsUIState.Loading -> { }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}