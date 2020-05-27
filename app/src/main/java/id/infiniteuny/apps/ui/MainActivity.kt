package id.infiniteuny.apps.ui

import android.Manifest
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.infiniteuny.apps.R
import id.infiniteuny.apps.data.gps.GpsService
import id.infiniteuny.apps.ui.maps.MapsView
import id.infiniteuny.apps.util.logD
import id.infiniteuny.apps.util.logE
import id.infiniteuny.apps.util.setupWithNavController
import org.osmdroid.config.Configuration

@Suppress(
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION",
    "DEPRECATED_IDENTITY_EQUALS"
)
class MainActivity : AppCompatActivity() {

    companion object{
        var mapsCommunicator: MapsView? =null
    }

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        window.applyTransparentStatusBar()
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        mapsShow()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            1
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState!!)

        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bot_navigation)

        val navGraphIds =
            listOf(R.navigation.home_nav, R.navigation.maps_nav, R.navigation.profile_nav)

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.fragment_container,
            intent = intent
        )

        controller.observe(this, Observer { navController ->
            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun mapsShow() {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {

                if (grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                    getLocate()
                } else {
                    this.finish()
                }
                return
            }
        }
    }

    private fun getLocate() {
        if (serviceRunning(GpsService::class.java)) {
          // toast("Service not Running")
        } else {
          // toast("Service is Running")
            val intent = Intent(applicationContext, GpsService::class.java)
            startService(intent)
            registerReceiver(broadcastReceiver, IntentFilter(GpsService.str_reciver))
        }
    }

    private fun serviceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 != null) {
                if (p1.getStringExtra("latitude") != null && p1.getStringExtra("longitude") != null) {
//                   if()
                    if(mapsCommunicator!=null){
                        mapsCommunicator!!.sendLocation(p1.getStringExtra("latitude").toDouble(),p1.getStringExtra("longitude").toDouble())
                        logD("communicator attach")
                    }
//                    MapsFragment.latitude=p1.getStringExtra("latitude").toDouble()
//                    MapsFragment.longitude=p1.getStringExtra("longitude").toDouble()
                }
            }else{

                logE("disable")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(broadcastReceiver, IntentFilter(GpsService.str_reciver))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

}
