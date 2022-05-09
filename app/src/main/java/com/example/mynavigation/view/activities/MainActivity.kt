package com.example.mynavigation.view.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mynavigation.R
import com.example.mynavigation.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
//
//        if (sharedPreferences.getString("SESSION_ID_KEY", null).isNullOrEmpty()){
//            Intent(this, LoginActivity::class.java).also {
//                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(it)
//            }
//        }



        navController = findNavController(R.id.fragment)

        initBottomNav()
        initOnDestinationChangedListener()

    }

    private fun initOnDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movieCatalogFragment,
                R.id.favouritesFragment,
                R.id.profileFragment ->{
                    binding.bottomNav.visibility = View.VISIBLE
                }
                R.id.movieDetail -> {
                    binding.bottomNav.visibility = View.GONE
                }
            }
        }
    }

    private fun initBottomNav() {
        binding.bottomNav.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        binding.bottomNav.setupWithNavController(navController)
    }
}