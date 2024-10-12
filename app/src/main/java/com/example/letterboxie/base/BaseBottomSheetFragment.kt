package com.example.letterboxie.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment < VB : ViewBinding >
    (private val bindingInflater : (inflater : LayoutInflater) -> VB) : BottomSheetDialogFragment() {
    private var _binding : VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        _binding = bindingInflater(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}