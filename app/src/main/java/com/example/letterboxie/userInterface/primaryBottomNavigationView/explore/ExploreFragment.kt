package com.example.letterboxie.userInterface.primaryBottomNavigationView.explore

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxie.R
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentExploreBinding
import com.example.letterboxie.models.base.Movie
import com.example.letterboxie.userInterface.adapters.SearchAdapter
import com.example.letterboxie.utilities.Constants.FORREST_GUMP
import com.example.letterboxie.utilities.gone
import com.example.letterboxie.utilities.showToastMessage
import com.example.letterboxie.utilities.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : BaseFragment < FragmentExploreBinding > (FragmentExploreBinding::inflate) {
    private val exploreViewModel by viewModels < ExploreViewModel > ()
    private val searchAdapter = SearchAdapter()
    private val empty = emptyList < Movie > ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewSearch.adapter = searchAdapter
        customizeSearchView()
        observeData()
        search()
        searchAdapter.onClick = { movieID ->
            findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToMovieDetailsFragment(movieID))
        }
        binding.textViewNotFound.setOnClickListener {
            findNavController().navigate(ExploreFragmentDirections.
            actionExploreFragmentToMovieDetailsFragment(FORREST_GUMP))
        }
    }

    private fun observeData() {
        exploreViewModel.nowPlayingUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ExploreUIState.Success -> searchAdapter.updateMovies(state.movies)
                is ExploreUIState.Error -> requireContext().showToastMessage(state.errorMessage)
                is ExploreUIState.Loading -> { }
            }
        }
        exploreViewModel.exploreUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ExploreUIState.Success -> {
                    binding.textViewNotFound.gone()
                    searchAdapter.updateMovies(state.movies)
                }
                is ExploreUIState.Error -> {
                    if (state.errorMessage == "Null or Empty!!") {
                        searchAdapter.updateMovies(empty)
                        binding.textViewNotFound.visible()
                    } else requireContext().showToastMessage(state.errorMessage)
                }
                is ExploreUIState.Loading -> { }
            }
        }
    }

    private fun search() {
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query : String?) : Boolean {
                query?.let { searchQuery -> exploreViewModel.search(searchQuery) }
                return true
            }

            override fun onQueryTextChange(newQuery : String?) : Boolean {
                newQuery?.let { searchQuery ->
                    if (binding.searchView.query.isEmpty()) {
                        binding.textViewNotFound.gone()
                        searchAdapter.updateMovies(empty)
                    }
                    else exploreViewModel.search(searchQuery)
                }
                return true
            }
        })

        binding.searchView.setOnQueryTextFocusChangeListener { _, focus ->
            if (focus) {
                if (binding.searchView.query.isEmpty()) searchAdapter.updateMovies(empty)
            } else {
                if (binding.searchView.query.isEmpty()) exploreViewModel.getNowPlayingMovies()
            }
        }
    }

    private fun customizeSearchView() {
        val searchView = binding.searchView
        val searchText = searchView.findViewById < EditText > (androidx.appcompat.R.id.search_src_text)
        searchText.apply {
            typeface = ResourcesCompat.getFont(requireContext(), R.font.open_sans_regular)
            textSize = 12f
            setHintTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
        val clearButton = searchView.findViewById < ImageView > (androidx.appcompat.R.id.search_close_btn)
        clearButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
    }
}