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
import androidx.navigation.Navigation
import com.example.mynavigation.databinding.FragmentMovieCatalogBinding
import com.example.mynavigation.presentation.view.adapters.MovieAdapter
import com.example.mynavigation.presentation.viewModel.MovieCatalogViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieCatalogFragment : Fragment() {
    private lateinit var binding: FragmentMovieCatalogBinding
    private val viewModel by viewModel<MovieCatalogViewModel>()
    private var viewModelAdapter: MovieAdapter? = null
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        private var sessionId: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieCatalogBinding.inflate(layoutInflater)
        sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(sessionId)
        getSessionId()
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

    private fun initRecyclerView() {
        viewModelAdapter = MovieAdapter(
            itemClickListener = viewModel.recyclerViewItemClickListener,
            markFavItemClick = viewModel.recyclerViewFavItemClick
        )
        binding.recyclerView.adapter = viewModelAdapter
    }

    private fun getSessionId() {
        try {
            sessionId = sharedPreferences?.getString("SESSION_ID_KEY", null) as String
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun observeViewModel() {

        viewModel.movieList.observe(viewLifecycleOwner) {
            when (it) {
                is MovieCatalogViewModel.State.ShowLoading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is MovieCatalogViewModel.State.HideLoading -> {
                    binding.swipeRefresh.isRefreshing = false
                }
                is MovieCatalogViewModel.State.Result -> {
                    viewModelAdapter?.submitList(it.list)
                }
            }
        }

        viewModel.openDetail.observe(
            viewLifecycleOwner
        ) {
            val action = it.getContentIfNotHandled()?.let { it1 ->
                Int
                MovieCatalogFragmentDirections.actionMovieCatalogFragmentToMovieDetail(
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
}
