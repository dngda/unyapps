package id.infiniteuny.apps.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("app:loadImageWithGlide")
fun loadImage(iv: ImageView, url: String) {
    Glide.with(iv.context)
        .load(url)
        .apply(RequestOptions.centerCropTransform())
        .into(iv)
}


@BindingAdapter("app:setTitle")
fun titleText(tv: TextView, s: String) {
    tv.text = s.capitalizeWords()
}

