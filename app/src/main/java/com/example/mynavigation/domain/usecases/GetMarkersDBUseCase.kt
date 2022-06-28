package com.example.mynavigation.domain.usecases

import com.example.mynavigation.domain.common.BaseUseCase
import com.example.mynavigation.domain.model.data.Marker
import com.example.mynavigation.domain.repositories.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class GetMarkersDBUseCase(private val repository: MapRepository) :
    BaseUseCase<List<Marker>, Any>() {
    override suspend fun run(params: Any?): List<Marker> {
        val list = withContext(Dispatchers.IO) {
            try {
                repository.getMarkers()
            } catch (e: Exception) {
                listOf()
            }
        }
        return list
    }
}