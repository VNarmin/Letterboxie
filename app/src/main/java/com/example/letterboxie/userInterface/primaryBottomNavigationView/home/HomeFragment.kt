package com.example.letterboxie.userInterface.primaryBottomNavigationView.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.letterboxie.R
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment < FragmentHomeBinding > (FragmentHomeBinding::inflate) {
    private val sharedViewModel by activityViewModels < SharedViewModel > ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeBottomNavigationView()
        observeMovieID()
    }

    private fun initializeBottomNavigationView() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragmentContainerViewHome) as NavHostFragment?
        navHostFragment?.let { fragment ->
            val navController = fragment.navController
            NavigationUI.setupWithNavController(binding.bottomNavigationViewHome, navController)
        }
    }

    private fun observeMovieID() {
        sharedViewModel.movieID.observe(viewLifecycleOwner) { movieID ->
            movieID?.let { mID ->
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(mID))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.resetMovieID()   // preventing the same request from being sent multiple times
    }
}