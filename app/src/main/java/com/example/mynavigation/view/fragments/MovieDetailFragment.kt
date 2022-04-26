package com.example.mynavigation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mynavigation.IMAGE_BASE
import com.example.mynavigation.databinding.FragmentMovieDetailBinding
import com.example.mynavigation.viewModel.MovieDetailViewModel
import com.example.mynavigation.viewModel.ViewModelProviderFactory


class MovieDetailFragment : Fragment() {
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.setArgs(args.movieId)
        viewModel.getMovie()
    }

    private fun initViewModel(){
        val viewModelProviderFactory = ViewModelProviderFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MovieDetailViewModel::class.java]

        viewModel.liveData.observe(viewLifecycleOwner){
            binding.tvDetailTitle.text = it.title
            binding.tvDetailOverview.text = it.overview
            Glide.with(requireContext()).load(IMAGE_BASE + it.poster)
                .into(binding.tvDetailPoster)
        }
    }

}