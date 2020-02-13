package id.infiniteuny.apps.ui.home.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import id.infiniteuny.apps.R
import kotlinx.android.synthetic.main.item_fakultas.view.*

class FacultyAdapter(private val list: List<Faculty>, private val context: Context?)
    : PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_img_slider, null)
        val title = view.adapter_fakultas

        //title.text = list[position].title

        container.addView(view, 0)

        return view

    }

}