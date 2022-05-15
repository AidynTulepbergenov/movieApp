package com.example.mynavigation.view.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mynavigation.R
import com.example.mynavigation.databinding.ActivityLoginBinding
import com.example.mynavigation.databinding.FragmentMovieDetailBinding
import com.example.mynavigation.databinding.FragmentProfileBinding
import com.example.mynavigation.view.activities.LoginActivity
import com.example.mynavigation.view.activities.MainActivity
import com.example.mynavigation.viewModel.MovieDetailViewModel
import com.example.mynavigation.viewModel.ProfileViewModel
import com.example.mynavigation.viewModel.ViewModelProviderFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        private var sessionId = ""
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = context?.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSessionId()
        initViewModel()
        setProfile()
        binding.exitButton.setOnClickListener {
            exitSession()
        }

        binding.btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }
        } else
            Toast.makeText(
                requireContext(),
                "Oops, you just denied the permission for camera. Don't worry you ca change it in the settings",
                Toast.LENGTH_SHORT
            ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST_CODE){
                val image: Bitmap = data!!.extras!!.get("data") as Bitmap
                binding.profilePhoto.setImageBitmap(image)
            }
        }
    }

    private fun getSessionId() {
        try {
            sessionId = sharedPreferences?.getString("SESSION_ID_KEY", null) as String
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }

    private fun setProfile() {
        val user = viewModel.setProfileDetails(sessionId)
        binding.data = user
    }

    private fun exitSession() {
        viewModel.deleteSession(sessionId)
        val editor = sharedPreferences?.edit()
        editor?.apply {
            remove("SESSION_ID_KEY")
        }?.apply()
        Intent(requireContext(), LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    private fun initViewModel() {
        val viewModelProviderFactory = ViewModelProviderFactory(requireContext())
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory)[ProfileViewModel::class.java]
    }

}