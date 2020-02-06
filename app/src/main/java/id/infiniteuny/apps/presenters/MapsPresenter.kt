package id.infiniteuny.apps.presenters

import android.content.Context
import com.google.gson.Gson
import id.ac.uny.utbk.data.model.Directions
import id.ac.uny.utbk.data.model.Gedung
import id.ac.uny.utbk.data.model.Mosque
import id.ac.uny.utbk.data.model.Park
import id.infiniteuny.apps.ui.maps.MapsView
import id.infiniteuny.apps.util.JsonReader
import id.infiniteuny.apps.util.RouteRequest
import id.infiniteuny.apps.util.logD
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Executors

class MapsPresenter(val view: MapsView, val context: Context) {
    private var data: MutableList<Gedung> = mutableListOf()
    private var musholas: MutableList<Mosque> = mutableListOf()
    private var parkirs: MutableList<Park> = mutableListOf()

    fun getBuilding() {
        data.addAll(JsonReader.readData(context))
        view.showData(data)
    }

    fun getMushola() {
        musholas.clear()
        musholas.addAll(JsonReader.readDataMosq(context))
        view.showDataMosq(musholas)
    }

    fun getParkir() {
        parkirs.clear()
        parkirs.addAll(JsonReader.readDataPark(context))
        view.showDataPark(parkirs)
    }

    fun getRoute(latO: Double, lngO: Double, latD: Double, lngD: Double, directionBy: String) {
        val url = RouteRequest.requestRoute(latO, lngO, latD, lngD, directionBy)
        val executor = Executors.newScheduledThreadPool(5)
            doAsync(executorService = executor) {
                val direction = Gson().fromJson(RouteRequest.requestHttp(url), Directions::class.java)
                uiThread {
                    logD("route size ${direction.paths.size}")
                    view.showRoute(direction.paths[0].points.coordinates, latD, lngD)
                }
            }

    }
}