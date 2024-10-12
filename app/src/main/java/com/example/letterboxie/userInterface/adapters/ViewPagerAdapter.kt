package com.example.letterboxie.userInterface.adapters

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import com.example.letterboxie.userInterface.detailScreens.cast.CastFragment
import com.example.letterboxie.userInterface.detailScreens.crew.CrewFragment
import com.example.letterboxie.userInterface.detailScreens.details.DetailsFragment

class ViewPagerAdapter(fragmentManager : FragmentManager,lifecycle : Lifecycle, private val movieID : Int)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val fragments = arrayListOf(CastFragment().apply { arguments = Bundle().apply { putInt("movieID", movieID) } },
        CrewFragment().apply { arguments = Bundle().apply { putInt("movieID", movieID) } },
        DetailsFragment().apply { arguments = Bundle().apply { putInt("movieID", movieID) } })

    override fun getItemCount() = fragments.size

    override fun createFragment(position : Int) = fragments[position]
}