package com.example.mynavigation.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mynavigation.IMAGE_BASE
import com.example.mynavigation.R
import com.example.mynavigation.databinding.FragmentMovieDetailBinding
import com.example.mynavigation.viewModel.MovieDetailViewModel
import com.example.mynavigation.viewModel.ViewModelProviderFactory


class MovieDetailFragment : Fragment() {
    private val args: MovieDetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel
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
        initViewModel()
        movieId = args.movieId
        viewModel.setArgs(args.movieId)

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

    private fun initViewModel() {
        val viewModelProviderFactory = ViewModelProviderFactory(requireContext())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[MovieDetailViewModel::class.java]

        viewModel.liveData.observe(viewLifecycleOwner) {
            binding.tvDetailTitle.text = it.title
            binding.tvDetailOverview.text = it.overview
            Glide.with(requireContext()).load(IMAGE_BASE + it.poster)
                .into(binding.tvDetailPoster)
        }

        viewModel.check.observe(viewLifecycleOwner) {
            if (it)
                binding.markButton.setImageResource(R.drawable.ic_star_black)
            else
                binding.markButton.setImageResource(R.drawable.ic_star_full)
        }
    }

    private fun addFavorite(movieId: Int, sessionId: String) {
        viewModel.markFavorite(movieId, sessionId)
        binding.markButton.setImageResource(R.drawable.ic_star_black)
    }

    private fun deleteFavorite(movieId: Int, sessionId: String) {
        viewModel.deleteFavorites(movieId, sessionId)
        binding.markButton.setImageResource(R.drawable.ic_star_full)
    }


    private fun onFavoriteClickListener() {
        binding.markButton.setOnClickListener {
            viewModel.checkFavorite(sessionId, movieId)
            viewModel.check.observe(viewLifecycleOwner) {
                if (it == false) {
                    addFavorite(movieId, sessionId)
                    Toast.makeText(context, "Added to \"Favorites\"", Toast.LENGTH_SHORT).show()
                } else {
                    deleteFavorite(movieId, sessionId)
                    Toast.makeText(context, "Removed from \"Favorites\"", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}