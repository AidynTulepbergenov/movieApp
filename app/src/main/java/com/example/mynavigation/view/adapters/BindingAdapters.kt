package com.example.mynavigation

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

const val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

@BindingAdapter("bindIconUrl")
fun bindIconUrl(imageView: ImageView, url: String?){
    Glide.with(imageView.context).load(IMAGE_BASE + url).into(imageView)
}