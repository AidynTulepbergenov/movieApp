package com.example.mynavigation.presentation.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mynavigation.databinding.ActivityLoginBinding
import com.example.mynavigation.domain.model.responses.LoginApprove
import com.example.mynavigation.presentation.viewModel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModel<LoginViewModel>()
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        observeViewModel()
        login()
    }

    private fun observeViewModel() {
        viewModel.sessionId.observe(this, Observer { it ->
            when (it) {
                is LoginViewModel.State.ShowLoading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                LoginViewModel.State.HideLoading -> {
                    binding.progressBar.visibility = View.GONE
                }
                is LoginViewModel.State.Result -> {
                    val editor = sharedPreferences?.edit()
                    editor?.apply {
                        putString("SESSION_ID_KEY", it.sessionId)
                    }?.apply()
                    Intent(this, MainActivity::class.java).also {
                        it.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        binding.progressBar.visibility = View.GONE
                        startActivity(it)
                    }
                }
                LoginViewModel.State.StatusFailed -> {
                    Toast.makeText(applicationContext, "Wrong data!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun login() {
        binding.btnLogin.setOnClickListener {
            val email: String = binding.etEmail.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val data = LoginApprove(
                username = email,
                password = pass,
                request_token = ""
            )
            viewModel.login(data)
        }
    }

}