package com.example.letterboxie.userInterface.primaryBottomNavigationView.profile.core

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxie.R
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentProfileBinding
import com.example.letterboxie.models.firestore.MoviePrimaryActions
import com.example.letterboxie.userInterface.adapters.MoviePrimaryActionsAdapter
import com.example.letterboxie.userInterface.adapters.WatchLaterAdapter
import com.example.letterboxie.utilities.showToastMessage
import com.example.letterboxie.utilities.visible
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment < FragmentProfileBinding > (FragmentProfileBinding::inflate) {
    private val profileViewModel by activityViewModels < ProfileViewModel > ()
    private val adapterFavorite = WatchLaterAdapter()
    private val adapterRecentActivity = MoviePrimaryActionsAdapter()
    @Inject
    lateinit var formatter : SimpleDateFormat

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewFavorites.adapter = adapterFavorite
        binding.recyclerViewRecentActivity.adapter = adapterRecentActivity
        observeData()
        binding.imageButtonMoviesExpand.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAlreadyWatchedFragment()) }
        binding.imageButtonReviewsExpand.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToUserReviewsFragment()) }
        binding.imageButtonLikesExpand.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLikedFragment()) }
        binding.imageButtonWatchlistExpand.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToWatchLaterFragment()) }
        binding.imageButtonLogout.setOnClickListener {
            profileViewModel.resetRememberMe()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToOnBoardingFragment())
        }
        adapterFavorite.onClick = { movieID ->
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMovieDetailsFragment(movieID)) }
        adapterRecentActivity.onClick = { movieID ->
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMovieDetailsFragment(movieID)) }
    }

    private fun observeData() {
        profileViewModel.username.observe(viewLifecycleOwner) { username ->
            binding.textViewUsername.text = username
        }
        profileViewModel.retrieveMovies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileUIState.Success -> success(state.collection)
                is ProfileUIState.Error -> requireContext().showToastMessage(state.errorMessage)
                is ProfileUIState.Loading -> { }
            }
        }
        profileViewModel.retrieveReviews.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileUIState.Success -> binding.textViewCountReviews.text = state.collection.size.toString()
                is ProfileUIState.Error -> requireContext().showToastMessage(state.errorMessage)
                is ProfileUIState.Loading -> { }
            }
        }
    }

    private fun success(movies : List < MoviePrimaryActions >) {
        val favorite = movies.filter { movie -> movie.favorite }
            .sortedBy { movie -> formatter.parse(movie.addedToFavoritesAt) }
        adapterFavorite.updateMovies(favorite.take(4))

        val alreadyWatched = movies.filter { movie -> movie.alreadyWatched }
            .sortedByDescending { movie -> formatter.parse(movie.addedToAlreadyWatchedAt) }
        adapterRecentActivity.updateMovies(alreadyWatched.take(4))

        binding.textViewCountMovies.text = alreadyWatched.size.toString()
        val liked = movies.filter { movie -> movie.liked }
        binding.textViewCountLikes.text = liked.size.toString()
        val watchLater = movies.filter { movie -> movie.watchLater }
        binding.textViewCountWatchlist.text = watchLater.size.toString()

        val userRatings = alreadyWatched.filter { movie -> movie.userRating != 0f}
            .map { movie -> movie.userRating }
        binding.barChartUserRatings.visible()
        customizeBarChart(userRatings)
    }

    private fun customizeBarChart(userRatings : List <Float >) {
        val chart = binding.barChartUserRatings
        val entries = getBarChartData(userRatings)
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

    private fun getBarChartData(userRatings : List < Float >) : ArrayList < BarEntry > {
        val possibleRatings = arrayListOf(0.5f, 1f, 1.5f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 5f)
        val ratingCounts = mutableMapOf < Float, Int > ().apply {
            possibleRatings.forEach { r -> this[r] = 1 } // Initialize the counts to zero
        }
        val barEntries = arrayListOf < BarEntry  > ()
        // Count how many times each user rating appears
        for (userRating in userRatings) {
            ratingCounts[userRating]?.let { count -> ratingCounts[userRating] = count + 1 }
        }
        // Create BarEntry objects based on the counts
        for ((index, rating) in possibleRatings.withIndex()) {
            val count = ratingCounts[rating] ?: 1
            val weight = count.toFloat() / userRatings.size.toFloat()
            barEntries.add(BarEntry(index.toFloat(), weight))
        }
        return barEntries
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.retrieveMovieCollection()
        profileViewModel.retrieveReviewCollection()
    }
}