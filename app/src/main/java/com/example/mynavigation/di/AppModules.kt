package com.example.mynavigation.di

import android.content.Context
import androidx.databinding.MapChangeRegistry
import com.example.mynavigation.data.db.MarkerDao
import com.example.mynavigation.data.db.MovieDao
import com.example.mynavigation.data.db.MovieDatabase
import com.example.mynavigation.data.network.RetrofitService
import com.example.mynavigation.domain.repositories.MapRepository
import com.example.mynavigation.domain.repositories.MapRepositoryImpl
import com.example.mynavigation.domain.repositories.MoviesRepositoryImpl
import com.example.mynavigation.domain.repositories.MoviesRepository
import com.example.mynavigation.domain.usecases.*
import com.example.mynavigation.presentation.viewModel.*
import com.google.android.material.shape.MarkerEdgeTreatment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val networkModule = module {
    single { getRetrofitService() }
}

val daoModule = module {
    single { getPostDao(context = get()) }
    single { getMarkerDao(context = get()) }
}

val repositoryModule = module {
    single<MoviesRepository> { MoviesRepositoryImpl(database = get(), service = get()) }
    single<MapRepository> { MapRepositoryImpl(markerDao = get()) }
}

val useCaseModule = module {
    single { GetMovieListUseCase(repository = get()) }
    single { GetMovieDetailUseCase(repository = get()) }
    single { MarkFavoriteUseCase(repository = get()) }
    single { LoginUseCase(repository = get()) }
    single { GetFavMovieListUseCase(repository = get()) }
    single { LogoutUseCase(repository = get()) }
    single { GetAccountDetailUseCase(repository = get()) }
    single { FillMarkerDBUseCase(repository = get()) }
    single { GetMarkersDBUseCase(repository = get()) }
}

val viewModelModule = module {
    viewModel { MovieCatalogViewModel(getMovieListUseCase = get(), markFavoriteUseCase = get()) }
    viewModel { MovieDetailViewModel(getMovieDetailUseCase = get(), markFavoriteUseCase = get()) }
    viewModel {
        FavouritesViewModel(
            app = get(),
            markFavoriteUseCase = get(),
            getFavMovieListUseCase = get()
        )
    }
    viewModel { LoginViewModel(loginUseCase = get()) }
    viewModel { ProfileViewModel(logoutUseCase = get(), getAccountDetailUseCase = get()) }
    viewModel { MapsViewModel(fillMarkerDBUseCase = get(), getMarkersDBUseCase = get()) }
}

val appModules = networkModule + daoModule + repositoryModule + viewModelModule + useCaseModule

private fun getRetrofitService() = RetrofitService
private fun getPostDao(context: Context): MovieDao = MovieDatabase.getDatabase(context).getDao()
private fun getMarkerDao(context: Context): MarkerDao =
    MovieDatabase.getDatabase(context).getMarkDao()
