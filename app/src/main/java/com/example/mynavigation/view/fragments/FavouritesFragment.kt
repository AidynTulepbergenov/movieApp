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
import com.example.mynavigation.databinding.FragmentFavouritesBinding
import com.example.mynavigation.view.adapters.MovieAdapter
import com.example.mynavigation.viewModel.FavouritesViewModel
import com.example.mynavigation.viewModel.MovieCatalogViewModel
import com.example.mynavigation.viewModel.ViewModelProviderFactory

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private var sharedPreferences: SharedPreferences? = null
    private lateinit var viewModel: FavouritesViewModel


    companion object {
        private var sessionId: String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        binding = FragmentFavouritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSessionId()

        initAndObserveViewModel()
        viewModel.setArgs(sessionId)

        binding.recyclerView.adapter = MovieAdapter(
            itemClickListener = viewModel.recyclerViewItemClickListener,
            markFavItemClick = viewModel.recyclerViewFavItemClick
        )
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFavList()
        }
        viewModel.getFavList()
    }

    private fun getSessionId() {
        try {
            sessionId = sharedPreferences?.getString("SESSION_ID_KEY", null) as String
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun initAndObserveViewModel() {
        val viewModelProviderFactory = ViewModelProviderFactory(requireContext())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[FavouritesViewModel::class.java]

        viewModel.favList.observe(
            viewLifecycleOwner
        ) {
            when (it) {
                is FavouritesViewModel.State.ShowLoading -> {
                    binding.swipeRefresh.isRefreshing = true
                }
                is FavouritesViewModel.State.HideLoading -> {
                    binding.swipeRefresh.isRefreshing = false
                }
                is FavouritesViewModel.State.Result -> {
                    (binding.recyclerView.adapter as MovieAdapter).submitList(it.list)
                }
            }
        }

        viewModel.openDetail.observe(
            viewLifecycleOwner
        ) {
            val action = it.getContentIfNotHandled()?.let { it1 ->
                Int
                FavouritesFragmentDirections.actionFavouritesFragmentToMovieDetail(
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