package com.example.mynavigation.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mynavigation.databinding.FragmentSimilarMovieBinding
import com.example.mynavigation.presentation.view.adapters.MovieAdapter
import com.example.mynavigation.presentation.viewModel.SimilarMovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SimilarMovieFragment : Fragment() {
    private lateinit var binding: FragmentSimilarMovieBinding
    private val viewModel by viewModel<SimilarMovieViewModel>()
    private var viewModelAdapter: MovieAdapter? = null

    companion object {
        private var movieId: Int = 0
        fun newInstance(movieId: Int): SimilarMovieFragment {
            this.movieId = movieId
            return SimilarMovieFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSimilarMovieBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(movieId)
        observeViewModel()
        initRecyclerView()
        setSwipe()
    }

    private fun setSwipe() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMovies()
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.getMovies()
    }

    private fun observeViewModel() {
        viewModel.movieList.observe(viewLifecycleOwner) {
            when (it) {
                is SimilarMovieViewModel.State.ShowLoading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is SimilarMovieViewModel.State.HideLoading -> {
                    binding.swipeRefresh.isRefreshing = false
                }
                is SimilarMovieViewModel.State.Result -> {
                    viewModelAdapter?.submitList(it.list)
                }
                is SimilarMovieViewModel.State.Error -> {
                    Toast.makeText(requireContext(), "Something went wrong, try again!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }

        viewModel.openDetail.observe(
            viewLifecycleOwner
        ) {
            val action = it.getContentIfNotHandled()?.let { it1 ->
                Int
                SimilarMovieFragmentDirections.actionSimilarMovieFragmentToViewPagerFragment2(
                    movieId = it1
                )
            }
            if (action != null) {
                Navigation.findNavController(binding.root).navigate(action)
            }
        }

        viewModel.markFavourite.observe(viewLifecycleOwner) {
            val movieId = it.getContentIfNotHandled()
            if (movieId != null) {
                viewModel.markFavorite(movieId)
            }
        }

        viewModel.isFavourite.observe(viewLifecycleOwner) {
            val isFav = it.getContentIfNotHandled()
            if (isFav == true)
                Toast.makeText(requireContext(), "Added to \"Favorites\"", Toast.LENGTH_SHORT)
                    .show()
            else if (isFav == false)
                Toast.makeText(requireContext(), "Removed from \"Favorites\"", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun initRecyclerView() {
        viewModelAdapter = MovieAdapter(
            itemClickListener = viewModel.recyclerViewItemClickListener,
            markFavItemClick = viewModel.recyclerViewFavItemClick
        )
        binding.recyclerView.adapter = viewModelAdapter
    }

}