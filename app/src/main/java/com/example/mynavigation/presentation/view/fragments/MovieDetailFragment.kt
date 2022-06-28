package com.example.mynavigation.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mynavigation.IMAGE_BASE
import com.example.mynavigation.databinding.FragmentMovieDetailBinding
import com.example.mynavigation.presentation.viewModel.MovieDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding
    private val viewModel by viewModel<MovieDetailViewModel>()

    companion object {
        private var movieId: Int = 0
        fun newInstance(movieId: Int) : MovieDetailFragment {
            this.movieId = movieId
            return MovieDetailFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.getMovie()
        onFavoriteClickListener()
    }


    private fun observeViewModel() {
        viewModel.setArgs(movieId)

        viewModel.movieData.observe(viewLifecycleOwner) {
            when (it) {
                is MovieDetailViewModel.State.ShowLoading ->
                    binding.progressBar.visibility = View.VISIBLE
                is MovieDetailViewModel.State.HideLoading ->
                    binding.progressBar.visibility = View.GONE
                is MovieDetailViewModel.State.Result -> {
                    binding.tvDetailTitle.text = it.movie.title
                    binding.tvDetailOverview.text = it.movie.overview
                    Glide.with(requireContext()).load(IMAGE_BASE + it.movie.poster)
                        .into(binding.tvDetailPoster)
                }
            }
        }

        viewModel.isFavourite.observe(viewLifecycleOwner) {
            if (it)
                Toast.makeText(requireContext(), "Added to \"Favorites\"", Toast.LENGTH_SHORT)
                    .show()
            else
                Toast.makeText(requireContext(), "Removed from \"Favorites\"", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun onFavoriteClickListener() {
        binding.markButton.setOnClickListener {
            viewModel.markFavorite()
        }
    }

}