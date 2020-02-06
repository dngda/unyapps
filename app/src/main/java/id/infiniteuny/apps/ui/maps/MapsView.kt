package id.infiniteuny.apps.ui.maps

import id.ac.uny.utbk.data.model.Gedung
import id.ac.uny.utbk.data.model.Mosque
import id.ac.uny.utbk.data.model.Park

interface MapsView {
    fun isLoading(state: Boolean)
    fun showData(data: List<Gedung>)
    fun showDataPark(data: List<Park>)
    fun showDataMosq(data: List<Mosque>)
    fun showRoute(data: List<List<Double>>?, destLat: Double, destLong: Double)
    fun sendLocation(latit:Double,longit:Double)
}