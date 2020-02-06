package id.infiniteuny.apps.data.gps

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import java.util.*
import android.R.string.cancel
import android.content.DialogInterface
import android.provider.Settings
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.widget.Toast


class GpsService : Service(), LocationListener {
    companion object {
        var str_reciver = "servicetutorial.service.receiver"
    }

    lateinit var intent: Intent
    var gpsEnabled = false
    var netEnable = false
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    lateinit var locationManager: LocationManager
    var location: Location? = null
    var handler = Handler()
    var timer: Timer? = null
    var showing=false
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        timer = Timer()
        timer!!.schedule(TimerTaskLocate(), 5, 1000)
        intent = Intent(str_reciver)
        //logD("created")
    }

    override fun onLocationChanged(p0: Location?) {
        intent.putExtra("latutide", location?.latitude.toString())
        intent.putExtra("longitude", location?.longitude.toString())
        sendBroadcast(intent)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {
        getLocate()
    }

    override fun onProviderDisabled(p0: String?) {

    }

    inner class TimerTaskLocate : TimerTask() {
        override fun run() {
            handler.post {
                getLocate()
            }
        }

    }

    @SuppressLint("MissingPermission")
    fun getLocate() {
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        netEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        when (gpsEnabled && netEnable) {
            true -> {
                if (netEnable) {
                   // logD("netEnable")
                    location = null
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0f, this)
                    when (locationManager != null) {
                        true -> {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            when (location != null) {
                                true -> {
                                   // logE("latitude ${location?.latitude.toString()}")
                                   // logE("longitude${location?.longitude.toString()}")
                                    latitude = location?.latitude!!
                                    longitude = location?.longitude!!
                                    updLocate()
                                }
                            }
                        }
                    }
                }
//                if(gpsEnabled){
//                    logD("gpsEnabled")
//                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
//                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//                    logD(location.toString())
//                    when (location != null) {
//                        true -> {
//                            Log.e("latitude", location?.getLatitude().toString() + "")
//                            Log.e("longitude", location?.getLongitude().toString() + "")
//                            latitude = location?.latitude!!
//                            longitude = location?.longitude!!
//                            updLocate()
//                        }
//                    }
//
//
//                }
            }
            else -> {
                if(!showing){
                    val dialogIntent = Intent(ACTION_LOCATION_SOURCE_SETTINGS)
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    Toast.makeText(this, "GPS Tidak Aktif, Mohon Aktifkan GPS Perangkat Anda!", Toast.LENGTH_SHORT).show()
                    showing=true
                    startActivity(dialogIntent)
                }
            }
        }
    }

    fun updLocate() {
        intent.putExtra("latitude", latitude.toString())
        intent.putExtra("longitude", longitude.toString())
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //logE("START SERVICE")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopSelf()
       // logE("STOP SERVICE")
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        super.onTaskRemoved(rootIntent)
    }
}