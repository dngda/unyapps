package id.infiniteuny.apps.ui.home.viewpager

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.PagerAdapter
import id.infiniteuny.apps.R
import id.infiniteuny.apps.ui.home.FacultyContentActivity
import id.infiniteuny.apps.ui.home.announcement.AnnouncementContentActivity
import id.infiniteuny.apps.ui.home.viewpager.Faculty.setupItem


class FacultyAdapter(private val context: Context) : PagerAdapter()  {

    private val LIBRARIES: Array<Faculty.LibraryObject> = arrayOf(
        Faculty.LibraryObject(
            R.raw.ft_uny,
            "Fakultas Teknik",
            "http://ft.uny.ac.id"
        ),
        Faculty.LibraryObject(
            R.raw.fmipa,
            "Fakultas Matematika dan IPA",
            "http://fmipa.uny.ac.id"
        ),
        Faculty.LibraryObject(
            R.raw.fis_uny,
            "Fakultas Ilmu Sosial",
            "http://fis.uny.ac.id"
        ),
        Faculty.LibraryObject(
            R.raw.fip_uny,
            "Fakultas Ilmu Pendidikan",
            "http://fip.uny.ac.id"
        ),
        Faculty.LibraryObject(
            R.raw.fik_uny,
            "Fakultas Ilmu Keolahragaan",
            "http://fik.uny.ac.id"
        ),
        Faculty.LibraryObject(
            R.raw.fe_uny,
            "Fakultas Ekonomi",
            "http://fe.uny.ac.id"
        ),
        Faculty.LibraryObject(
            R.raw.pasca_uny,
            "Pasca Sarjana","http://pps.uny.ac.id"
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
        layout.setOnClickListener {
            Intent(context, FacultyContentActivity::class.java).also {
                it.putExtra("link", LIBRARIES[position].url)
                context.startActivity(it)
            }
        }
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