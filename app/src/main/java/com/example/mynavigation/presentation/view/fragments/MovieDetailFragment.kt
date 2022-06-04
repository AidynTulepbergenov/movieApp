package com.example.mynavigation.presentation.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mynavigation.IMAGE_BASE
import com.example.mynavigation.databinding.FragmentMovieDetailBinding
import com.example.mynavigation.presentation.viewModel.MovieDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailFragment : Fragment() {
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentMovieDetailBinding
    private val viewModel by viewModel<MovieDetailViewModel>()
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        private var sessionId: String = ""
        private var movieId: Int = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        binding = FragmentMovieDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSessionId()
        observeViewModel()
        viewModel.getMovie()
        onFavoriteClickListener()
    }

    private fun getSessionId() {
        try {
            sessionId = sharedPreferences?.getString("SESSION_ID_KEY", null) as String
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun observeViewModel() {
        movieId = args.movieId
        viewModel.setArgs(args.movieId)

        viewModel.movieData.observe(viewLifecycleOwner) {
            binding.tvDetailTitle.text = it.title
            binding.tvDetailOverview.text = it.overview
            Glide.with(requireContext()).load(IMAGE_BASE + it.poster)
                .into(binding.tvDetailPoster)
        }

        viewModel.isFavourite.observe(viewLifecycleOwner) {
            if (it)
                Toast.makeText(requireContext(), "Added to \"Favorites\"", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(requireContext(), "Removed from \"Favorites\"", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onFavoriteClickListener() {
        binding.markButton.setOnClickListener {
            viewModel.markFavorite()
        }
    }

}