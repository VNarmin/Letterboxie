package com.example.letterboxie.userInterface.bottomSheets

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.letterboxie.R
import com.example.letterboxie.base.BaseBottomSheetFragment
import com.example.letterboxie.databinding.FragmentPrimaryActionsBottomSheetBinding
import com.example.letterboxie.models.firestore.MoviePrimaryActions
import com.example.letterboxie.models.firestore.UserReview
import com.example.letterboxie.utilities.showToastMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class PrimaryActionsBottomSheetFragment : BaseBottomSheetFragment < FragmentPrimaryActionsBottomSheetBinding >
    (FragmentPrimaryActionsBottomSheetBinding::inflate) {
    private val primaryActionsBottomSheetViewModel by viewModels < PrimaryActionsBottomSheetViewModel > ()
    private val args by navArgs < PrimaryActionsBottomSheetFragmentArgs > ()
    @Inject
    lateinit var simpleDateTimeFormatter : SimpleDateFormat

    private var favorite = false
    private var addedToFavoritesAt = ""
    private var alreadyWatched = false
    private var addedToAlreadyWatchedAt = ""
    private var liked = false
    private var watchLater = false
    private var addedToWatchLaterAt = ""
    private var rating = 0f

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        primaryActionsBottomSheetViewModel.retrieveMovieDocument(args.movieCore.movieID.toString())
        binding.imageButtonFavorite.setOnClickListener { onClickFavorite() }
        binding.imageButtonWatch.setOnClickListener { onClickAlreadyWatched() }
        binding.imageButtonLike.setOnClickListener { onClickLiked() }
        binding.imageButtonWatchLater.setOnClickListener { onClickWatchLater() }
        binding.textViewDone.setOnClickListener { onClickDone() }
    }

    private fun onClickDone() {
        val movie = MoviePrimaryActions(documentID = args.movieCore.movieID.toString(),
            favorite = favorite, addedToFavoritesAt = addedToFavoritesAt,
            alreadyWatched = alreadyWatched, addedToAlreadyWatchedAt = addedToAlreadyWatchedAt,
            liked = liked, watchLater = watchLater, addedToWatchLaterAt = addedToWatchLaterAt,
            userRating = binding.ratingBar.rating, movieCore = args.movieCore)
        if (!binding.textReviewInput.text.isNullOrEmpty()) {
            val reviewContent = binding.textReviewInput.text.toString().trim()
            val savedAt = simpleDateTimeFormatter.format(Date(System.currentTimeMillis()))
            val review = UserReview(documentID = args.movieCore.movieID.toString(),
                userRating = binding.ratingBar.rating, liked = liked, reviewContent = reviewContent,
                savedAt = savedAt, movieCore = args.movieCore)
            primaryActionsBottomSheetViewModel.saveReviewDocument(review)
        }
        dismiss()
        if (movie.favorite || movie.alreadyWatched || movie.liked || movie.watchLater || movie.userRating != 0f) {
            primaryActionsBottomSheetViewModel.saveMovieDocument(movie)   // either saving or updating data
        } else {   // after resetting all boolean values
            primaryActionsBottomSheetViewModel.deleteMovieDocument(args.movieCore.movieID.toString())
        }
    }

    private fun observeData() {
        primaryActionsBottomSheetViewModel.retrieveMovie.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BottomSheetUIState.Success -> {
                    favorite = state.documentData.favorite
                    addedToFavoritesAt = state.documentData.addedToFavoritesAt
                    favoriteInitialState()
                    alreadyWatched = state.documentData.alreadyWatched
                    addedToAlreadyWatchedAt = state.documentData.addedToAlreadyWatchedAt
                    alreadyWatchedInitialState()
                    liked = state.documentData.liked
                    likedInitialState()
                    watchLater = state.documentData.watchLater
                    addedToWatchLaterAt = state.documentData.addedToWatchLaterAt
                    watchLaterInitialState()
                    rating = state.documentData.userRating
                    binding.ratingBar.rating = rating
                }
                is BottomSheetUIState.Error -> { }
                is BottomSheetUIState.Loading -> { }
            }
        }
        primaryActionsBottomSheetViewModel.errorSaveMovie.observe(viewLifecycleOwner) { errorMessage ->
            requireContext().showToastMessage(errorMessage) }
        primaryActionsBottomSheetViewModel.errorSaveReview.observe(viewLifecycleOwner) { errorMessage ->
            requireContext().showToastMessage(errorMessage) }
        primaryActionsBottomSheetViewModel.errorDeleteMovie.observe(viewLifecycleOwner) { errorMessage ->
            requireContext().showToastMessage(errorMessage) }
    }

    private fun onClickFavorite() {
        if (!favorite) {
            favorite = true
            addedToFavoritesAt = simpleDateTimeFormatter.format(Date(System.currentTimeMillis()))
            binding.imageButtonFavorite.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.favorite_pressed))
        } else {
            favorite = false
            addedToFavoritesAt = ""
            binding.imageButtonFavorite.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.favorite_default))
        }
    }

    private fun favoriteInitialState() {
        if (favorite) binding.imageButtonFavorite.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.favorite_pressed))
    }

    private fun onClickAlreadyWatched() {
        if (!alreadyWatched) {
            alreadyWatched = true
            addedToAlreadyWatchedAt = simpleDateTimeFormatter.format(Date(System.currentTimeMillis()))
            binding.imageButtonWatch.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.already_watched_pressed))
            binding.textViewWatch.text = ContextCompat.getString(requireContext(), R.string.already_watched_pressed)
        } else {
            alreadyWatched = false
            addedToAlreadyWatchedAt = ""
            binding.imageButtonWatch.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.already_watched_default))
            binding.textViewWatch.text = ContextCompat.getString(requireContext(), R.string.already_watched_default)
        }
    }

    private fun alreadyWatchedInitialState() {
        if (alreadyWatched) {
            binding.imageButtonWatch.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.already_watched_pressed))
            binding.textViewWatch.text = ContextCompat.getString(requireContext(), R.string.already_watched_pressed)
        }
    }

    private fun onClickLiked() {
        if (!liked) {
            liked = true
            binding.imageButtonLike.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.liked_pressed))
            binding.textViewLike.text = ContextCompat.getString(requireContext(), R.string.liked_pressed)
        } else {
            liked = false
            binding.imageButtonLike.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.liked_default))
            binding.textViewLike.text = ContextCompat.getString(requireContext(), R.string.liked_default)
        }
    }

    private fun likedInitialState() {
        if (liked) {
            binding.imageButtonLike.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.liked_pressed))
            binding.textViewLike.text = ContextCompat.getString(requireContext(), R.string.liked_pressed)
        }
    }

    private fun onClickWatchLater() {
        if (!watchLater) {
            watchLater = true
            addedToWatchLaterAt = simpleDateTimeFormatter.format(Date(System.currentTimeMillis()))
            binding.imageButtonWatchLater.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.watch_later_pressed))
        } else {
            watchLater = false
            addedToWatchLaterAt = ""
            binding.imageButtonWatchLater.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                R.drawable.watch_later_default))
        }
    }

    private fun watchLaterInitialState() {
        if (watchLater) binding.imageButtonWatchLater.setImageDrawable(ContextCompat.getDrawable(requireContext(),
            R.drawable.watch_later_pressed))
    }
}