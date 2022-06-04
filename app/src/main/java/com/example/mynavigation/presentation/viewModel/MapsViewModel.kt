package com.example.mynavigation.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynavigation.domain.common.UseCaseResponse
import com.example.mynavigation.domain.model.ApiError
import com.example.mynavigation.domain.model.data.Marker
import com.example.mynavigation.domain.model.data.generateMarkers
import com.example.mynavigation.domain.repositories.MapRepository
import com.example.mynavigation.domain.usecases.FillMarkerDBUseCase
import com.example.mynavigation.domain.usecases.GetMarkersDBUseCase
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MapsViewModel(
    private val fillMarkerDBUseCase: FillMarkerDBUseCase,
    private val getMarkersDBUseCase: GetMarkersDBUseCase
) : ViewModel(), CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private var _markers = MutableLiveData<List<Marker>>()
    val markers: LiveData<List<Marker>>
        get() = _markers

    fun fillDatabase() {
        fillMarkerDBUseCase.invoke(viewModelScope, null, object : UseCaseResponse<Any> {
            override fun onSuccess(result: Any) {
                Log.d("DB_MARKER", "Filling db succeeded")
            }

            override fun onError(apiError: ApiError?) {
                Log.d("DB_MARKER", "Filling db failed")
            }
        })
    }


    fun getMarkersFromDatabase() {
        getMarkersDBUseCase.invoke(viewModelScope, null, object : UseCaseResponse<List<Marker>> {
            override fun onSuccess(result: List<Marker>) {
                _markers.value = result
            }

            override fun onError(apiError: ApiError?) {
                Log.d("DB_MARKER", "Getting markers failed" + apiError.toString())
            }

        })
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}