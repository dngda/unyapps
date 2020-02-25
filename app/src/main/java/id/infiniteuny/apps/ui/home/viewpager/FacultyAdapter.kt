package id.infiniteuny.apps.ui.home.viewpager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import id.infiniteuny.apps.R
import id.infiniteuny.apps.ui.home.viewpager.Faculty.setupItem

class FacultyAdapter(private val context: Context) : PagerAdapter()  {

    private val LIBRARIES: Array<Faculty.LibraryObject> = arrayOf(
        Faculty.LibraryObject(
            R.raw.ft_uny,
            "Fakultas Teknik"
        ),
        Faculty.LibraryObject(
            R.raw.fmipa,
            "Fakultas Matematika dan IPA"
        ),
        Faculty.LibraryObject(
            R.raw.fis_uny,
            "Fakultas Ilmu Sosial"
        ),
        Faculty.LibraryObject(
            R.raw.fip_uny,
            "Fakultas Ilmu Pendidikan"
        ),
        Faculty.LibraryObject(
            R.raw.fik_uny,
            "Fakultas Ilmu Keolahragaan"
        ),
        Faculty.LibraryObject(
            R.raw.fe_uny,
            "Fakultas Ekonomi"
        ),
        Faculty.LibraryObject(
            R.raw.pasca_uny,
            "Pasca Sarjana"
        )
    )


    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.item_fakultas, container, false) as ViewGroup
        container.addView(layout)
        setupItem(layout, LIBRARIES[position])
        return layout
    }



    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return LIBRARIES.size

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as View)
    }


}