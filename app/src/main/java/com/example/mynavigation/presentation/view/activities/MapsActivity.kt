package com.example.mynavigation.presentation.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mynavigation.R
import com.example.mynavigation.databinding.ActivityMapsBinding
import com.example.mynavigation.domain.model.data.Marker
import com.example.mynavigation.presentation.viewModel.MapsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModel<MapsViewModel>()
    private var markers: List<Marker> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fillDatabase()
        viewModel.getMarkersFromDatabase()
        viewModel.markers.observe(this) {
            markers = it
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(mapReadyCallback)
        }

    }

    private val mapReadyCallback = OnMapReadyCallback {
        mMap = it

        if (!markers.isNullOrEmpty()) {
            for (marker in markers) {
                val mapMarker = LatLng(marker.lat, marker.lng)
                mMap.addMarker(MarkerOptions().position(mapMarker).title(marker.title))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(marker.lat, marker.lng)))
            }
            val zoomLevel = 14.0f
            val marker = markers[markers.size - 1]
            val latLng = LatLng(marker.lat, marker.lng)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
        }
    }
}