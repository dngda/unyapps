package id.infiniteuny.apps.ui.maps


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.android.material.floatingactionbutton.FloatingActionButton
import id.ac.uny.utbk.data.model.Gedung
import id.ac.uny.utbk.data.model.Halte
import id.ac.uny.utbk.data.model.Mosque
import id.ac.uny.utbk.data.model.Park
import id.infiniteuny.apps.R
import id.infiniteuny.apps.presenters.MapsPresenter
import id.infiniteuny.apps.ui.MainActivity
import id.infiniteuny.apps.util.logD
import id.infiniteuny.apps.util.logE
import id.infiniteuny.apps.util.toast
import id.infiniteuny.apps.util.toastCenter
import kotlinx.android.synthetic.main.maps_fragment.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*


class MapsFragment : Fragment(), MapsView {

    override fun sendLocation(latit: Double, longit: Double) {
        latitude = latit
        longitude = longit
        logD("send loc ${latitude},${longitude}")
        if (latitude != 0.0 && longitude != 0.0) {
            userMarker()
        }

    }

    companion object {
        var buildingTrigger: Gedung? = null
    }

    private fun userMarker() {
        if (latitude != 0.0 && longitude != 0.0) {
            Log.i("test", "userMark $isCentered")
            if (!isCentered) {
                setCenterMap()
                logD("centered")
                isCentered = true
            } else {

                logD("not centered")
            }
            userMarker.position = GeoPoint(latitude, longitude)
            userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            userMarker.title = "Posisi Anda"
            userMarker.icon = requireContext().getDrawable(R.drawable.personmarker)
            map.overlays.remove(userMarker)
            map.overlays.add(userMarker)
        }
    }

    private var isCentered = true
    override fun showRoute(data: List<List<Double>>?, destLat: Double, destLong: Double) {
        showMosq = false
        showBuilding = false
        showPark = false
        markering()
        map.invalidate()
        val geoPoints = ArrayList<GeoPoint>()
        data?.forEach {
            geoPoints.add(GeoPoint(it[1], it[0]))
        }
        routingResult = geoPoints
        val line = Polyline()
        line.setPoints(geoPoints)
        line.outlinePaint.color = requireContext().getColor(R.color.colorPrimary)
        line.outlinePaint.strokeWidth = 15f
        firstRoute = true
        reDrawOnOverlay(line)
    }

    private var latitude: Double = -7.774745865534583
    private var longitude: Double = 110.38623690605164
    private fun reDrawOnOverlay(line: Polyline) {
        map.overlayManager.clear()
        val destMarker = Marker(map)
        val buildMarker = Marker(map)
        destMarker.position = GeoPoint(line.points[line.points.size - 1])
        destMarker.icon = requireContext().getDrawable(R.drawable.ic_location_target)
        destMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userMarker.position = GeoPoint(latitude, longitude)
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        buildMarker.position = GeoPoint(buildLat, buildLong)
        buildMarker.icon =
            this.requireContext().getDrawable(R.drawable.ic_location_transparant)
        buildMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        buildMarker.title = "tujuan"
        map.overlays.add(buildMarker)
        map.overlays.add(userMarker)
        map.overlays.add(destMarker)
        map.overlays.add(line)
        if (firstRoute) {
            firstRoute = false
            mapController.animateTo(GeoPoint(latitude, longitude))
            mapController.setCenter(GeoPoint(latitude, longitude))
        }
    }

    private var routingResult: MutableList<GeoPoint>? = mutableListOf()
    private lateinit var map: MapView
    private lateinit var myLocationOverlay: MyLocationNewOverlay
    private lateinit var presenter: MapsPresenter
    private lateinit var mapController: IMapController
    private lateinit var searchView: FloatingSearchView
    private var showMosq: Boolean = false
    private var destLat: Double = 0.0
    private var destLong: Double = 0.0
    private var buildLat: Double = 0.0
    private var buildLong: Double = 0.0
    private var typeRoute: String = ""
    private lateinit var userMarker: Marker
    private lateinit var infoLayout: ConstraintLayout
    private lateinit var gedungName: TextView
    private lateinit var roomName: TextView
    private lateinit var directBtn: LinearLayout
    private lateinit var fabUser: FloatingActionButton
    private lateinit var fabMosq: FloatingActionButton
    private lateinit var fabHalte: FloatingActionButton
    private var dataGedung: MutableList<Gedung> = mutableListOf()
    private var searhData: MutableList<Gedung> = mutableListOf()
    private var dataPark: MutableList<Park> = mutableListOf()
    private var dataMosque: MutableList<Mosque> = mutableListOf()
    private var dataHalte: MutableList<Halte> = mutableListOf()
    private lateinit var rootView: View
    private var showPark: Boolean = false
    private var showBuilding: Boolean = false
    private var firstRoute = false
    private var showHalte: Boolean = false
    override fun isLoading(state: Boolean) {

    }

    override fun showData(data: List<Gedung>) {
        dataGedung.clear()
        dataGedung.addAll(data)

    }

    override fun showDataPark(data: List<Park>) {
        dataPark.clear()
        dataPark.addAll(data)
    }

    override fun showDataMosq(data: List<Mosque>) {
        dataMosque.clear()
        logE(data.size.toString())
        dataMosque.addAll(data)

    }

    override fun showDataHalte(data: List<Halte>) {
        dataHalte.clear()
        logE(data.size.toString())
        dataHalte.addAll(data)
    }
/*
    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        MainActivity.mapsCommunicator=this
    }

 */

    private fun markerBuilding() {
        showBuilding = true
        dataGedung.forEach { gedung ->
            val buildingMarker = Marker(map)
            buildingMarker.icon =
                requireContext().getDrawable(R.drawable.ic_location_building)
            buildingMarker.position = GeoPoint(gedung.latitude, gedung.longitude)
            buildingMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            buildingMarker.setOnMarkerClickListener { _, _ ->
                showDetail(gedung)
                true
            }
            map.overlays.add(buildingMarker)

        }
    }

    private fun markerMushola() {
        showMosq = true
        dataMosque.forEach { mosq ->
            val mosqueMarker = Marker(map)
            mosqueMarker.position = GeoPoint(mosq.latitude, mosq.longitude)
            mosqueMarker.icon =
                requireContext().getDrawable(R.drawable.ic_location_mosque)
            mosqueMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mosqueMarker.title = mosq.mosqueName
            map.overlays.add(mosqueMarker)
            directGmaps.visibility = View.GONE
            mosqueMarker.setOnMarkerClickListener { _, _ ->
                infoLayout.visibility = View.VISIBLE
                gedungName.text = mosq.mosqueName
                val text: TextView = rootView.findViewById(R.id.roomAvv)
                text.text = ""
                directBtn.setOnClickListener {
                    infoLayout.visibility = View.GONE
                    laytipe.visibility = View.VISIBLE
                    this.destLat = mosq.latitude
                    this.destLong = mosq.longitude
                    requireContext().toast("Pilih type kendaraan anda")

                }
                roomName.text = ""
                return@setOnMarkerClickListener true
            }
        }
    }

    private fun markerHalte() {
        showHalte = true
        dataHalte.forEach { halte ->
            val halteMarker = Marker(map)
            halteMarker.position = GeoPoint(halte.latitude, halte.longitude)
            halteMarker.icon =
                requireContext().getDrawable(R.drawable.ic_location_bus)
            halteMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            halteMarker.title = halte.halteName
            map.overlays.add(halteMarker)
            directGmaps.visibility = View.GONE
            halteMarker.setOnMarkerClickListener { _, _ ->
                infoLayout.visibility = View.VISIBLE
                gedungName.text = halte.halteName
                val text: TextView = rootView.findViewById(R.id.roomAvv)
                text.text = ""
                directBtn.setOnClickListener {
                    infoLayout.visibility = View.GONE
                    laytipe.visibility = View.VISIBLE
                    this.destLat = halte.latitude
                    this.destLong = halte.longitude
                    requireContext().toast("Pilih type kendaraan anda")

                }
                roomName.text = ""
                return@setOnMarkerClickListener true
            }
        }
    }

    private fun markerParkir() {
        showPark = true
        dataPark.forEach { park ->
            val parkingMarker = Marker(map)
            parkingMarker.icon =
                requireContext().getDrawable(R.drawable.ic_location_parking)
            parkingMarker.position = GeoPoint(park.latitude, park.longitude)
            parkingMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            parkingMarker.title = park.parkirName
            parkingMarker.setOnMarkerClickListener { _, _ ->
                infoLayout.visibility = View.VISIBLE
                gedungName.text = park.parkirName
                val text: TextView = rootView.findViewById(R.id.roomAvv)
                text.text = ""
                directGmaps.visibility = View.GONE
                directBtn.setOnClickListener {
                    infoLayout.visibility = View.GONE
                    laytipe.visibility = View.VISIBLE
                    this.destLat = park.latitude
                    this.destLong = park.longitude
                    requireContext().toast("Pilih type kendaraan anda")
                }
                roomName.text = ""
                return@setOnMarkerClickListener true
            }
            map.overlays.add(parkingMarker)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        rootView = inflater.inflate(R.layout.maps_fragment, container, false)
        map = rootView.findViewById(R.id.mapview)
        userMarker = Marker(map)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            supportActionBar?.hide()
        }

        Configuration.getInstance().userAgentValue = this.requireActivity().packageName
        searchView = view.findViewById(R.id.searching)
        infoLayout = view.findViewById(R.id.info)
        gedungName = view.findViewById(R.id.gedung_name)
        roomName = view.findViewById(R.id.tvRoom)
        directBtn = view.findViewById(R.id.directLayout)
        fabUser = view.findViewById(R.id.fabmyLocation)
        fabMosq = view.findViewById(R.id.fabMosque)
        fabHalte = view.findViewById(R.id.fabHalte)
        Configuration.getInstance().userAgentValue = requireContext().packageName
        view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        fabUser.setOnClickListener {
            userMarker()
            setCenterMap()
        }
        fabMosq.setOnClickListener {
            when (showMosq) {
                true -> {
                    map.overlays.clear()
                    userMarker()
                    showMosq = false
                    markering()
                    map.invalidate()
                }
                false -> {
                    map.overlays.clear()
                    userMarker()
                    showMosq = true
                    markering()
                    map.invalidate()
                }
            }
            map.invalidate()
        }
        fabBuilding.setOnClickListener {
            when (showBuilding) {
                true -> {
                    map.overlays.clear()
                    userMarker()
                    showBuilding = false
                    markering()
                    map.invalidate()
                }
                false -> {
                    map.overlays.clear()
                    userMarker()
                    showBuilding = true
                    markering()
                }
            }
            map.invalidate()
        }
        fabParking.setOnClickListener {
            when (showPark) {
                true -> {
                    map.overlays.clear()
                    userMarker()
                    showPark = false
                    markering()
                    map.invalidate()
                }
                false -> {
                    map.overlays.clear()
                    userMarker()
                    showPark = true
                    markering()
                    map.invalidate()
                }
            }
            map.invalidate()
        }
        fabHalte.setOnClickListener {
            when (showHalte) {
                true -> {
                    map.overlays.clear()
                    userMarker()
                    showHalte = false
                    markering()
                    map.invalidate()
                }
                false -> {
                    map.overlays.clear()
                    userMarker()
                    showHalte = true
                    markering()
                    map.invalidate()
                }
            }
            map.invalidate()
        }

        fabwalk.setOnClickListener {
            this.typeRoute = "foot"
            laytipe.visibility = View.GONE
            requireContext().toastCenter("Mencari Route...")
            routing(this.destLat, this.destLong)
        }
        fabcar.setOnClickListener {
            this.typeRoute = "car"
            laytipe.visibility = View.GONE
            requireContext().toastCenter("Mencari Route...")
            routing(this.destLat, this.destLong)
        }
        fabmoto.setOnClickListener {
            this.typeRoute = "car"
            laytipe.visibility = View.GONE
            requireContext().toastCenter("Mencari Route...")
            routing(this.destLat, this.destLong)
        }
        fabbike.setOnClickListener {
            this.typeRoute = "bike"
            laytipe.visibility = View.GONE
            requireContext().toastCenter("Mencari Route...")
            routing(this.destLat, this.destLong)
        }
        init()


    }

    private fun markering() {
        if (routingResult!!.size > 0) {
            val line = Polyline()
            line.setPoints(routingResult)
            line.outlinePaint.color = requireContext().getColor(R.color.colorPrimary)
            line.outlinePaint.strokeWidth = 15f
            reDrawOnOverlay(line)
        }
        if (showMosq) {
            markerMushola()
        }
        if (showBuilding) {
            markerBuilding()
        }
        if (showPark) {
            markerParkir()
        }
        if (showHalte) {
            markerHalte()
        }


    }


    private fun searchingData(param: String?): MutableList<Gedung> {
        val data: MutableList<Gedung> = mutableListOf()
        if (dataGedung.size > 0) {
            dataGedung.forEach { gedung ->
                if (param != null) {
                    if (gedung.namaGedung.toLowerCase(Locale.ROOT)
                            .contains(param.toLowerCase(Locale.ROOT))
                    ) {
                        data.add(gedung)
                    }
                }

            }
        }
        return data
    }

    private fun initMap() {
        map.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        map.setMultiTouchControls(true)
        map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
    }

    private fun initController() {
        mapController = map.controller
        mapController.setZoom(18.0)

    }

    private fun initMyLocation() {
        myLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this.context), map)
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.myLocation
        map.overlays.add(myLocationOverlay)
    }

    private fun setCenterMap() {
        if (buildingTrigger != null) {
            logD("data")
            mapController.animateTo(
                GeoPoint(
                    buildingTrigger!!.latitude,
                    buildingTrigger!!.longitude
                )
            )
            mapController.setCenter(
                GeoPoint(
                    buildingTrigger!!.latitude,
                    buildingTrigger!!.longitude
                )
            )
            mapController.setZoom(16.0)
        } else {
            mapController.animateTo(GeoPoint(latitude, longitude))
            mapController.setCenter(GeoPoint(latitude, longitude))
        }


    }

    private fun routing(destLat: Double, destLong: Double) {
        presenter.getRoute(latitude, longitude, destLat, destLong, this.typeRoute)
    }

    private fun init() {
        initMap()
        initController()
        setCenterMap()
        //initMyLocation()
        presenter = MapsPresenter(this, this.requireContext())
        presenter.getBuilding()
        presenter.getMushola()
        presenter.getHalte()
        presenter.getParkir()
        if (buildingTrigger != null) {
            logD("datais${buildingTrigger!!.ruangs}")
            val building = buildingTrigger
            isCentered = true
            logE(isCentered.toString())
            searchShowMarker(building!!.namaGedung)
        } else {
            isCentered = false
        }




        searchView.setOnQueryChangeListener { _, newQuery ->
            searhData.clear()
            if (newQuery.isNotEmpty() && newQuery.length > 2) {
                searhData.addAll(searchingData(newQuery))
                searchView.swapSuggestions(searhData)
            } else {
                searchView.clearSuggestions()
            }
        }

        searchView.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String?) {

            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion?) {
                if (searchSuggestion != null) {
                    val str = searchSuggestion.body.split("(")
                    searchView.setSearchText(searchSuggestion.body)
                    searchView.clearSearchFocus()
                    searchShowMarker(str[0].trim())
                }

            }

        })


    }

    private fun searchShowMarker(param: String?) {

        if (dataGedung.size > 0) {
            dataGedung.forEach { gedung ->
                if (param != null) {
                    if (gedung.namaGedung.toLowerCase(Locale.ROOT)
                            .contains(param.toLowerCase(Locale.ROOT))
                    ) {
                        showDetail(gedung)
                        mapController.setZoom(16.0)
                        map.overlays.clear()
                        val marker = Marker(map)
                        marker.icon =
                            this.requireContext().getDrawable(R.drawable.ic_location_target)
                        marker.position = GeoPoint(gedung.latitude, gedung.longitude)
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        map.overlays.add(marker)
                        mapController.animateTo(GeoPoint(gedung.latitude, gedung.longitude))
                        mapController.setCenter(GeoPoint(gedung.latitude, gedung.longitude))
                        isCentered = true
                    }
                }

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDetail(gedung: Gedung) {
        infoLayout.visibility = View.VISIBLE
        gedungName.text = gedung.namaGedung.toUpperCase(Locale.ROOT)
        directGmaps.visibility = View.VISIBLE
        directGmaps.setOnClickListener {
            val uri = "https://www.google.com/maps/place/${gedung.url_gmaps}"
            requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
        directBtn.setOnClickListener {
            infoLayout.visibility = View.GONE
            laytipe.visibility = View.VISIBLE
            this.destLat = gedung.lat_route
            this.buildLat = gedung.latitude
            this.buildLong = gedung.longitude
            this.destLong = gedung.long_route
            requireContext().toastCenter("Pilih type kendaraan anda")
            buildingTrigger = null
        }
        roomName.text = ""
        gedung.ruangs.forEach { ruang ->
            roomName.text = "${ruang.namaRuang} \n ${roomName.text}"
        }
        isCentered = true
    }

    private fun resizeImage(drawable: Drawable): Drawable {
        val image = (drawable as BitmapDrawable).bitmap
        val resizedImage = Bitmap.createScaledBitmap(image, 50, 50, false)
        return BitmapDrawable(requireContext().resources, resizedImage)
    }

    override fun onResume() {
        super.onResume()
        MainActivity.mapsCommunicator = this
    }

    override fun onPause() {
        super.onPause()
        buildingTrigger = null
        MainActivity.mapsCommunicator = null
        buildingTrigger = null
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivity.mapsCommunicator = null
        buildingTrigger = null
    }

    override fun onDetach() {
        super.onDetach()
        MainActivity.mapsCommunicator = null
        buildingTrigger = null
    }

}
