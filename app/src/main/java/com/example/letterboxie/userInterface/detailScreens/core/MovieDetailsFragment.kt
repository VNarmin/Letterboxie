package com.example.letterboxie.userInterface.detailScreens.core

import android.content.res.Resources.NotFoundException
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.letterboxie.R
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentMovieDetailsBinding
import com.example.letterboxie.models.base.MovieCore
import com.example.letterboxie.userInterface.adapters.MovieAdapter
import com.example.letterboxie.userInterface.adapters.ViewPagerAdapter
import com.example.letterboxie.userInterface.detailScreens.cast.CastViewModel
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.utilities.showToastMessage
import com.example.letterboxie.utilities.visible
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs
import kotlin.random.Random

@AndroidEntryPoint
class MovieDetailsFragment : BaseFragment < FragmentMovieDetailsBinding > (FragmentMovieDetailsBinding::inflate) {
    private val movieDetailsViewModel by viewModels < MovieDetailsViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()
    private val castViewModel by activityViewModels < CastViewModel > ()
    private val args by navArgs < MovieDetailsFragmentArgs > ()
    private val adapterRelated = MovieAdapter()
    private val adapterSimilar = MovieAdapter()
    private lateinit var movieCore : MovieCore

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewRelatedFilms.adapter = adapterRelated
        binding.recyclerViewSimilarFilms.adapter = adapterSimilar
        manageTabLayoutViewPager()
        observeData()
        observe()
        movieDetailsViewModel.fetchMovieDetails(args.movieID)

        adapterRelated.onClick = { movieID ->
            findNavController().navigate(MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(movieID)) }
        adapterSimilar.onClick = { movieID ->
            findNavController().navigate(MovieDetailsFragmentDirections.actionMovieDetailsFragmentSelf(movieID)) }

        binding.imageButtonPrimaryActions.setOnClickListener {
            findNavController().navigate(MovieDetailsFragmentDirections.
            actionMovieDetailsFragmentToPrimaryActionsBottomSheetFragment(movieCore)) }
        binding.imageButtonBack.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    private fun linkTabLayoutWithViewPager() {
        TabLayoutMediator(binding.tabLayout, binding.viewPagerMovieCreditsAndDetails) { tab, position ->
            tab.text = when (position) {
                0 -> "Cast"
                1 -> "Crew"
                2 -> "Details"
                else -> throw NotFoundException("Position not found!!")
            }
        }.attach()
    }

    private fun manageTabLayoutViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle, args.movieID)
        binding.viewPagerMovieCreditsAndDetails.adapter = viewPagerAdapter
        linkTabLayoutWithViewPager()
    }

    private fun observe() {
        movieDetailsViewModel.collectionID.observe(viewLifecycleOwner) { collectionID ->
            collectionID?.let { cID -> movieDetailsViewModel.getRelatedMovies(cID) }
        }
        movieDetailsViewModel.movieCore.observe(viewLifecycleOwner) { core -> movieCore = core }
        castViewModel.movieDirector.observe(viewLifecycleOwner) { movieDirector ->
            movieDirector?.let { mDirector -> binding.textViewDirector.text = mDirector }
        }
    }

    private fun observeData() {
        movieDetailsViewModel.movieDetailsUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MovieDetailsUIState.Success -> {
                    binding.movieDetails = state.movieDetails
                    state.movieDetails.voteAverage?.let { averageRating ->
                        binding.barChartAverageRating.visible()
                        customizeBarChart((averageRating / 2).toFloat()) }
                    sharedViewModel.setProgressAnimationVisibility(false)
                }
                is MovieDetailsUIState.Error -> {
                    requireContext().showToastMessage(state.errorMessage)
                    sharedViewModel.setProgressAnimationVisibility(false)
                }
                is MovieDetailsUIState.Loading -> sharedViewModel.setProgressAnimationVisibility(true)
            }
        }
        movieDetailsViewModel.relatedUIState.observe(viewLifecycleOwner) { state ->
            manageRequestStates(state = state, view = binding.viewRelated, textView = binding.textViewRelatedFilms,
                recyclerView = binding.recyclerViewRelatedFilms, movieAdapter = adapterRelated)
        }
        movieDetailsViewModel.similarUIState.observe(viewLifecycleOwner) { state ->
            manageRequestStates(state = state, view = binding.viewSimilar, textView = binding.textViewSimilarFilms,
                recyclerView = binding.recyclerViewSimilarFilms, movieAdapter = adapterSimilar)
        }
    }

    private fun manageRequestStates(state : RelatedSimilarUIState, view : View, textView : TextView,
                                    recyclerView : RecyclerView, movieAdapter : MovieAdapter) {
        when (state) {
            is RelatedSimilarUIState.Success -> {
                view.visible()
                textView.visible()
                recyclerView.visible()
                movieAdapter.updateMovies(state.movies)
            }
            is RelatedSimilarUIState.Error -> { }     // nothing
            is RelatedSimilarUIState.Loading -> { }   // nothing
        }
    }

    private fun customizeBarChart(averageRating : Float) {
        val chart = binding.barChartAverageRating
        val entries = getBarChartData(averageRating)
        val dataSet = BarDataSet(entries, "Ratings")
        dataSet.apply {
            color = ContextCompat.getColor(requireContext(), R.color.bar_color)
            barBorderColor = ContextCompat.getColor(requireContext(), R.color.bar_color)
            setDrawValues(false)
        }
        val barData = BarData(dataSet)
        barData.apply { barWidth = 0.96f }
        chart.apply {
            data = barData
            description.isEnabled = false
            legend.isEnabled = false
            axisLeft.apply {
                setDrawAxisLine(false)
                setDrawGridLines(false)
                setDrawLabels(false)
            }
            axisRight.apply {
                setDrawAxisLine(false)
                setDrawGridLines(false)
                setDrawLabels(false)
            }
            xAxis.apply {
                setDrawAxisLine(false)
                setDrawGridLines(false)
                setDrawLabels(false)
            }
            invalidate()
        }
    }

    private fun getBarChartData(averageRating : Float) : ArrayList < BarEntry > {
        val possibleRatings = arrayListOf(0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f)
        val barEntries = arrayListOf < BarEntry > ()
        val random = Random(System.currentTimeMillis())
        for ((index, rating) in possibleRatings.withIndex()) {
            if (averageRating == 0f) barEntries.add(BarEntry(index.toFloat(), 0.25f))
            else {
                val distance = abs(rating - averageRating)
                val proximityFactor = 1f - distance / possibleRatings.max()   // how close to the average rating
                val randomFactor = random.nextFloat() * 0.25f   // a random factor between 0.0 and 0.25
                val barWeight = proximityFactor + randomFactor
                barEntries.add(BarEntry(index.toFloat(), barWeight))
            }
        }
        return barEntries
    }
}