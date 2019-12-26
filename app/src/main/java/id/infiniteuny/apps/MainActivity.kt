package id.infiniteuny.apps

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.infiniteuny.apps.ui.HomeFragment
import id.infiniteuny.apps.ui.MapsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener ,
    HomeFragment.OnFragmentInteractionListener,
    MapsFragment.OnFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bot_navigation.setOnNavigationItemSelectedListener(this)
         }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId){
            R.id.nav_home -> {

            }
            R.id.nav_map -> {

            }
            R.id.nav_profile -> {

            }
        }
        return true
    }

    private fun loadFragment(fragment: Fragment?): Boolean {

        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

}
