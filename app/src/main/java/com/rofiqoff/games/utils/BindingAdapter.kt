package com.rofiqoff.games.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.rofiqoff.games.R

@BindingAdapter("imageUrl")
fun setImageUrl(view: AppCompatImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .error(R.drawable.ic_broken_image)
        .into(view)
}
