package com.example.letterboxie.userInterface.detailScreens.details

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentDetailsBinding
import com.example.letterboxie.models.base.MovieInfo
import com.example.letterboxie.userInterface.adapters.MovieInfoAdapter
import com.example.letterboxie.userInterface.detailScreens.cast.AlternativeTitlesUIState
import com.example.letterboxie.userInterface.detailScreens.cast.CastViewModel
import com.example.letterboxie.userInterface.detailScreens.core.MovieDetailsUIState
import com.example.letterboxie.utilities.showToastMessage
import com.example.letterboxie.utilities.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : BaseFragment < FragmentDetailsBinding > (FragmentDetailsBinding::inflate) {
    private val castViewModel by activityViewModels < CastViewModel > ()
    private val adapterGenre = MovieInfoAdapter()
    private val adapterProductionCompany = MovieInfoAdapter()
    private val adapterProductionCountry = MovieInfoAdapter()
    private val adapterSpokenLanguage = MovieInfoAdapter()
    private val adapterAlternativeTitle = MovieInfoAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        observeData()
    }

    private fun observeData() {
        castViewModel.movieDetailsUIState.observe(viewLifecycleOwner) { movieDetailsUIState ->
            movieDetailsUIState?.let { state -> when (state) {
                    is MovieDetailsUIState.Success -> {
                        state.movieDetails.genres?.let { genres ->
                            success(adapter = adapterGenre, data = genres, textView = binding.textViewGenres,
                                recyclerView = binding.recyclerViewGenres) }
                        state.movieDetails.productionCompanies?.let { productionCompanies ->
                            success(adapter = adapterProductionCompany, data = productionCompanies,
                                textView = binding.textViewProductionCompanies,
                                recyclerView = binding.recyclerViewProductionCompanies) }
                        state.movieDetails.productionCountries?.let { productionCountries ->
                            success(adapter = adapterProductionCountry, data = productionCountries,
                                textView = binding.textViewProductionCountries,
                                recyclerView = binding.recyclerViewProductionCountries) }
                        state.movieDetails.languages?.let { spokenLanguages ->
                            success(adapter = adapterSpokenLanguage, data = spokenLanguages,
                                textView = binding.textViewSpokenLanguages,
                                recyclerView = binding.recyclerViewSpokenLanguages) }
                    }
                    is MovieDetailsUIState.Error -> requireContext().showToastMessage(state.errorMessage)
                    is MovieDetailsUIState.Loading -> { }
                }
            }
        }
        castViewModel.alternativeTitlesUIState.observe(viewLifecycleOwner) { alternativeTitlesUIState ->
            alternativeTitlesUIState?.let { state -> when (state) {
                    is AlternativeTitlesUIState.Success -> {
                        success(adapter = adapterAlternativeTitle, data = state.alternativeTitles,
                            textView = binding.textViewAlternativeTitles,
                            recyclerView = binding.recyclerViewAlternativeTitles)
                    }
                    is AlternativeTitlesUIState.Error -> { }
                    is AlternativeTitlesUIState.Loading -> { }
                }
            }
        }
    }

    private fun success(adapter : MovieInfoAdapter, data : List < MovieInfo >,
                        textView : TextView, recyclerView : RecyclerView) {
        textView.visible()
        recyclerView.visible()
        adapter.updateMovieInfo(data)
    }

    private fun setAdapters() {
        binding.recyclerViewGenres.adapter = adapterGenre
        binding.recyclerViewProductionCompanies.adapter = adapterProductionCompany
        binding.recyclerViewProductionCountries.adapter = adapterProductionCountry
        binding.recyclerViewSpokenLanguages.adapter = adapterSpokenLanguage
        binding.recyclerViewAlternativeTitles.adapter = adapterAlternativeTitle
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}