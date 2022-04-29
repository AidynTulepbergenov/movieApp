package com.example.mynavigation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.mynavigation.databinding.FragmentMovieCatalogBinding
import com.example.mynavigation.view.adapters.MovieAdapter
import com.example.mynavigation.viewModel.MovieCatalogViewModel
import com.example.mynavigation.viewModel.ViewModelProviderFactory

class MovieCatalogFragment : Fragment(){

    private lateinit var binding: FragmentMovieCatalogBinding
    private lateinit var viewModel: MovieCatalogViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieCatalogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMovies()
        }
        binding.recyclerView.adapter = MovieAdapter(itemClickListener = viewModel.recyclerViewItemClickListener)
        viewModel.getMovies()
    }

    private fun initViewModel(){
        val viewModelProviderFactory = ViewModelProviderFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MovieCatalogViewModel::class.java]

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
            }
        }

        viewModel.openDetail.observe(
            viewLifecycleOwner
        ){
            val action = it.getContentIfNotHandled()?.let { it1 -> Int
                MovieCatalogFragmentDirections.actionMovieCatalogFragmentToMovieDetail(
                    movieId = it1
                )
            }
            if (action != null) {
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

}