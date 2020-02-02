package id.infiniteuny.apps.data.db.entities

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.infiniteuny.apps.util.capitalizeWords


@Entity
data class News(
    var title: String? = null,
    var summary: String? = null,
    @PrimaryKey(autoGenerate = false)
    var link: String,
    var content: String? = null,
    var thumbnail: String? = null,
    var created_date: String? = null,
    val featured_image: String? = null
) {
    companion object {
        @JvmStatic
        @BindingAdapter("app:thumbImage")
        fun thumbImage(iv: ImageView, url: String) {
            Glide.with(iv.context)
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .into(iv)
        }

        @JvmStatic
        @BindingAdapter("app:setTitle")
        fun titleText(tv: TextView, s: String) {
            tv.text = s.capitalizeWords()
        }
    }

}