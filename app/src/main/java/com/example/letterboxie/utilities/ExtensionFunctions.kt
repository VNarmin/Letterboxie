package com.example.letterboxie.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.letterboxie.databinding.CustomToastBinding

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Context.showToastMessage(toastMessage : String) {
    val binding = CustomToastBinding.inflate(LayoutInflater.from(this))
    binding.toastMessage.text = toastMessage
    val toast = Toast(this).apply {
        view = binding.root
        duration = Toast.LENGTH_SHORT
    }
    toast.show()
}

fun ImageView.setImage(imageURL : String?, placeholder : Int, error : Int) {
    Glide.with(this.context).load(imageURL).
    apply(RequestOptions().placeholder(placeholder).error(error)).into(this)
}