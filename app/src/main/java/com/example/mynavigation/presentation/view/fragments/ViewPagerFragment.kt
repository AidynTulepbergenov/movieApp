package com.example.mynavigation.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.mynavigation.databinding.FragmentViewPagerBinding
import com.example.mynavigation.presentation.view.adapters.VPAdapter
import com.google.android.material.tabs.TabLayoutMediator

val tabArray = arrayOf("Details", "Reviews", "Similar")

class ViewPagerFragment: Fragment() {
    private lateinit var binding: FragmentViewPagerBinding
    private val args: ViewPagerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPagerBinding.inflate(layoutInflater)

        val adapter = VPAdapter(requireActivity().supportFragmentManager, requireActivity().lifecycle, args.movieId)
        binding.viewPager.adapter = adapter

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

        return binding.root
    }
}