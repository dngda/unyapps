package id.infiniteuny.apps.ui.home.viewpager

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import id.infiniteuny.apps.R
import kotlinx.android.synthetic.main.item_fakultas.view.*
import org.jetbrains.anko.singleLine

object Faculty {
    fun setupItem(view: View, libraryObject: LibraryObject) {
        val txt = view.findViewById<View>(R.id.adapter_fakultas) as TextView
        txt.text = libraryObject.title
        txt.singleLine = false
        val img = view.findViewById<View>(R.id.adapter_backImage) as ImageView
        Glide.with(view).load(libraryObject.res).centerCrop().into(img)
     }

    class LibraryObject(var res: Int, var title: String )
}

