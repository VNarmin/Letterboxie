package com.example.letterboxie.userInterface.authScreens.onBoarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentOnBoardingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment < FragmentOnBoardingBinding > (FragmentOnBoardingBinding::inflate) {
    @Inject
    lateinit var sp : SharedPreferences

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rememberMe = retrieveRememberMe()
        if (rememberMe) {
            findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment())
        }
        binding.buttonGetStarted.setOnClickListener {
            findNavController().navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment())
        }
    }

    private fun retrieveRememberMe() : Boolean {
        return sp.getBoolean("remember_me", false)
    }
}