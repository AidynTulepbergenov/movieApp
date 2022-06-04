package com.example.mynavigation

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

const val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

@BindingAdapter("isNetworkError", "playlist")
fun hideIfNetworkError(view: View, isNetWorkError: Boolean, playlist: Any?) {
    view.visibility = if (playlist != null) View.GONE else View.VISIBLE

    if(isNetWorkError) {
        view.visibility = View.GONE
    }
}

@BindingAdapter("bindIconUrl")
fun bindIconUrl(imageView: ImageView, url: String?){
    Glide.with(imageView.context).load(IMAGE_BASE + url).into(imageView)
}