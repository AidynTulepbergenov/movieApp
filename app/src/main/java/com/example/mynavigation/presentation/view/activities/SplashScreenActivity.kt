package com.example.mynavigation.presentation.view.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mynavigation.R
import com.example.mynavigation.databinding.ActivityLoginBinding
import com.example.mynavigation.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        supportActionBar?.hide()

        checkLogin()
    }

    private fun checkLogin(){
        binding.progressBar.visibility = View.VISIBLE
        if (!sharedPreferences?.getString("SESSION_ID_KEY", null).isNullOrEmpty()) {
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                binding.progressBar.visibility = View.GONE
            }
        } else {
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                binding.progressBar.visibility = View.GONE
            }
        }
    }

}