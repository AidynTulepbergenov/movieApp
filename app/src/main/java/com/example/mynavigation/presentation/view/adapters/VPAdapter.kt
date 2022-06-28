package com.example.mynavigation.presentation.view.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mynavigation.presentation.view.fragments.MovieDetailFragment
import com.example.mynavigation.presentation.view.fragments.MovieReviewFragment
import com.example.mynavigation.presentation.view.fragments.SimilarMovieFragment

private const val NUM_TABS = 3

class VPAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, movieId: Int) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val id = movieId

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0-> {
                MovieDetailFragment.newInstance(movieId = id)
            }
            1-> {
                MovieReviewFragment.newInstance(movieId = id)
            }
            2-> {
                SimilarMovieFragment.newInstance(movieId = id)
            }
            else -> {
                Fragment()
            }
        }
    }

}