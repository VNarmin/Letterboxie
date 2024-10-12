package com.example.letterboxie.userInterface.authScreens.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentLoginBinding
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.utilities.gone
import com.example.letterboxie.utilities.showToastMessage
import com.example.letterboxie.utilities.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment < FragmentLoginBinding > (FragmentLoginBinding::inflate) {
    private val loginViewModel by viewModels < LoginViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        binding.textViewSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }
        binding.buttonLogin.setOnClickListener { getCredentials() }
    }

    private fun observeData() {
        loginViewModel.loginUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoginUIState.Success -> {
                    requireContext().showToastMessage("Authentication Successful!!")
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    sharedViewModel.setProgressAnimationVisibility(false)
                }
                is LoginUIState.Error -> {
                    requireContext().showToastMessage(state.errorMessage)
                    binding.loginEmailInput.text = null
                    binding.loginPasswordInput.text = null
                    progressAnimationOff()
                }
                is LoginUIState.Loading -> progressAnimationOn()

            }
        }
    }

    private fun getCredentials() {
        if (binding.loginEmailInput.text.isNullOrEmpty() || binding.loginPasswordInput.text.isNullOrEmpty()) {
            requireContext().showToastMessage("Missing Fields!!")
            return
        }
        val email = binding.loginEmailInput.text.toString().trim()
        val password = binding.loginPasswordInput.text.toString().trim()
        val rememberMe = binding.checkBoxLoginRememberMeLogin.isChecked
        loginViewModel.authenticate(email, password, rememberMe)
    }

    private fun progressAnimationOn() {
        binding.imageViewLogoLetterboxie.gone()
        binding.textViewLogoLetterboxie.gone()
        sharedViewModel.setProgressAnimationVisibility(true)
    }

    private fun progressAnimationOff() {
        sharedViewModel.setProgressAnimationVisibility(false)
        binding.imageViewLogoLetterboxie.visible()
        binding.textViewLogoLetterboxie.visible()
    }
}