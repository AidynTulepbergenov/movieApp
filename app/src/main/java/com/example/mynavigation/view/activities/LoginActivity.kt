package com.example.mynavigation.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mynavigation.R
import com.example.mynavigation.databinding.ActivityLoginBinding
import com.example.mynavigation.model.network.LoginApprove
import com.example.mynavigation.viewModel.LoginViewModel
import com.example.mynavigation.viewModel.MovieCatalogViewModel
import com.example.mynavigation.viewModel.ViewModelProviderFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        initViewModel()
        checkLogin()
    }

    private fun initViewModel() {
        val viewModelProviderFactory = ViewModelProviderFactory(baseContext)
        viewModel =
            ViewModelProvider(
                this,
                viewModelProviderFactory
            )[LoginViewModel::class.java]
    }

    private fun checkLogin() {
        binding.progressBar.visibility = View.VISIBLE
        if (!sharedPreferences?.getString("SESSION_ID_KEY", null).isNullOrEmpty()) {
            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                binding.progressBar.visibility = View.GONE
            }
        } else {
            binding.progressBar.visibility = View.GONE
            login()
        }

    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            val email: String = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            binding.progressBar.visibility = View.VISIBLE

            val data = LoginApprove(
                username = email,
                password = pass,
                request_token = ""
            )
            viewModel.login(data)

            viewModel.sessionId.observe(this, Observer {
                val editor = sharedPreferences?.edit()
                editor?.apply {
                    putString("SESSION_ID_KEY", it)
                }?.apply()
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    binding.progressBar.visibility = View.GONE
                    startActivity(it)
                }
            })
        }
    }

}