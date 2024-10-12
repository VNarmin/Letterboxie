package com.example.letterboxie.userInterface.authScreens.signUp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.letterboxie.base.BaseFragment
import com.example.letterboxie.databinding.FragmentSignUpBinding
import com.example.letterboxie.userInterface.primaryBottomNavigationView.home.SharedViewModel
import com.example.letterboxie.utilities.gone
import com.example.letterboxie.utilities.showToastMessage
import com.example.letterboxie.utilities.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment < FragmentSignUpBinding > (FragmentSignUpBinding::inflate) {
    private val signUpViewModel by viewModels < SignUpViewModel > ()
    private val sharedViewModel by activityViewModels < SharedViewModel > ()

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        binding.textViewLogin.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }
        binding.buttonSignUp.setOnClickListener { getCredentials() }
    }

    private fun observeData() {
        signUpViewModel.signUpUIState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignUpUIState.Success -> {
                    requireContext().showToastMessage("Registration Successful!!")
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
                    sharedViewModel.setProgressAnimationVisibility(false)
                }
                is SignUpUIState.Error -> {
                    requireContext().showToastMessage(state.errorMessage)
                    binding.signUpUsernameInput.text = null
                    binding.signUpEmailInput.text = null
                    binding.signUpPasswordInput.text = null
                    progressAnimationOff()
                }
                is SignUpUIState.Loading -> progressAnimationOn()
            }
        }
        signUpViewModel.userProfileSave.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignUpUIState.Success -> { }
                is SignUpUIState.Error -> requireContext().showToastMessage(state.errorMessage)
                is SignUpUIState.Loading -> { }
            }
        }
    }

    private fun getCredentials() {
        if (binding.signUpUsernameInput.text.isNullOrEmpty() || binding.signUpEmailInput.text.isNullOrEmpty() ||
            binding.signUpPasswordInput.text.isNullOrEmpty()) {
            requireContext().showToastMessage("Missing Fields!!")
            return
        }
        val username = binding.signUpUsernameInput.text.toString().trim()
        val email = binding.signUpEmailInput.text.toString().trim()
        val password = binding.signUpPasswordInput.text.toString().trim()
        val rememberMe = binding.checkBoxRememberMeSignUp.isChecked
        signUpViewModel.register(username, email, password, rememberMe)
    }

    private fun progressAnimationOn() {
        binding.imageViewLetterboxieLogo.gone()
        binding.textViewLetterboxieLogo.gone()
        sharedViewModel.setProgressAnimationVisibility(true)
    }

    private fun progressAnimationOff() {
        sharedViewModel.setProgressAnimationVisibility(false)
        binding.imageViewLetterboxieLogo.visible()
        binding.textViewLetterboxieLogo.visible()
    }
}