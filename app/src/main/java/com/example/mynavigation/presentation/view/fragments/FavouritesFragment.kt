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
import com.example.mynavigation.databinding.FragmentFavouritesBinding
import com.example.mynavigation.presentation.view.adapters.MovieAdapter
import com.example.mynavigation.presentation.viewModel.FavouritesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private var sharedPreferences: SharedPreferences? = null
    private val viewModel by viewModel<FavouritesViewModel>()
    private var viewModelAdapter: MovieAdapter? = null



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
        initRecyclerView()
        setSwipe()
    }

    private fun getSessionId() {
        try {
            sessionId = sharedPreferences?.getString("SESSION_ID_KEY", null) as String
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun initRecyclerView() {
        viewModelAdapter = MovieAdapter(
            itemClickListener = viewModel.recyclerViewItemClickListener,
            markFavItemClick = viewModel.recyclerViewFavItemClick
        )
        binding.recyclerView.adapter = viewModelAdapter
    }

    private fun setSwipe() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getFavList()
        }
        viewModel.getFavList()
    }

    private fun initAndObserveViewModel() {
        viewModel.setArgs(sessionId)
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