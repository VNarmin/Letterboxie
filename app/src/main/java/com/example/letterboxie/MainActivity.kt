package com.example.letterboxie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.letterboxie.databinding.ActivityMainBinding
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.utilities.gone
import com.example.letterboxie.utilities.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val sharedViewModel by viewModels < SharedViewModel > ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeBottomNavigationView()
        observeLoading()
    }

    private fun initializeBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment?
        navHostFragment?.let { fragment ->
            val navController = fragment.navController
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                handleBottomNavigationViewVisibility(destination.id)
            }
        }
    }

    private fun handleBottomNavigationViewVisibility(destinationID : Int) {
        when (destinationID) {
            R.id.onBoardingFragment, R.id.loginFragment, R.id.signUpFragment -> {
                binding.bottomNavigationView.gone()
            }
            else -> binding.bottomNavigationView.visible()
        }
    }

    private fun observeLoading() {
        sharedViewModel.loading.observe(this, Observer { loading ->
            if (loading) binding.progressAnimation.visible() else binding.progressAnimation.gone()
        })
    }
}