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
import androidx.navigation.Navigation
import com.example.mynavigation.R
import com.example.mynavigation.databinding.FragmentMovieCatalogBinding
import com.example.mynavigation.view.adapters.MovieAdapter
import com.example.mynavigation.viewModel.MovieCatalogViewModel
import com.example.mynavigation.viewModel.ViewModelProviderFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MovieCatalogFragment : Fragment() {
    private lateinit var binding: FragmentMovieCatalogBinding
    private lateinit var viewModel: MovieCatalogViewModel
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

        initViewModel()
        getSessionId()
        viewModel.setArgs(sessionId)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMovies()
        }
        binding.recyclerView.adapter = MovieAdapter(
            itemClickListener = viewModel.recyclerViewItemClickListener,
            markFavItemClick = viewModel.recyclerViewFavItemClick
        )

        viewModel.getMovies()
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
            ViewModelProvider(this, viewModelProviderFactory)[MovieCatalogViewModel::class.java]

        viewModel.movieList.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is MovieCatalogViewModel.State.ShowLoading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is MovieCatalogViewModel.State.HideLoading -> {
                    binding.swipeRefresh.isRefreshing = false
                }
                is MovieCatalogViewModel.State.Result -> {
                    (binding.recyclerView.adapter as MovieAdapter).submitList(it.list)
                }
                else -> {}
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
    }
}
