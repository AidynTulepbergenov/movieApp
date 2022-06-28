package com.example.mynavigation.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mynavigation.databinding.FragmentMovieReviewBinding
import com.example.mynavigation.presentation.view.adapters.CommentAdapter
import com.example.mynavigation.presentation.viewModel.MovieReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieReviewFragment : Fragment() {
    private lateinit var binding: FragmentMovieReviewBinding
    private val viewModel by viewModel<MovieReviewViewModel>()
    private var viewModelAdapter: CommentAdapter? = null


    companion object {
        private var movieId: Int = 0
        fun newInstance(movieId: Int) : MovieReviewFragment {
            this.movieId = movieId
            return MovieReviewFragment()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(movieId)
        observeViewModel()
        initRecyclerView()
        setSwipe()
    }

    private fun observeViewModel(){
        viewModel.reviews.observe(viewLifecycleOwner) {
            when (it) {
                MovieReviewViewModel.State.ShowLoading -> binding.swipeRefresh.isRefreshing = true
                MovieReviewViewModel.State.HideLoading -> binding.swipeRefresh.isRefreshing = false
                is MovieReviewViewModel.State.Result -> viewModelAdapter?.submitList(it.list)
                MovieReviewViewModel.State.Error -> Toast.makeText(requireContext(), "Something went wrong, try again!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initRecyclerView() {
        viewModelAdapter = CommentAdapter()
        binding.recyclerView.adapter = viewModelAdapter
    }

    private fun setSwipe() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getReviews()
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.getReviews()
    }
}