package com.example.mynavigation.viewModel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.mynavigation.model.data.Event
import com.example.mynavigation.model.data.Movie

class MovieCatalogViewModelObserver (
    private val context: Context,
    private val viewModel: MovieCatalogViewModel,
    private val viewLifecycleOwner: LifecycleOwner,

    private val openDetail: ((movieId: Event<Int>) -> Unit),
    private val liveData: ((state: MovieCatalogViewModel.State) -> Unit)
    ) {

        init {
            observeViewModel()
        }

        private fun observeViewModel() {
            liveData.apply {
                viewModel.movieList.observe(
                    viewLifecycleOwner
                ) {
                    this.invoke(it)
                }
            }

            openDetail.apply {
                viewModel.openDetail.observe(
                    viewLifecycleOwner
                ) {
                    this.invoke(it)
                }
            }
        }
}