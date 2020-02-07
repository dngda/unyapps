package id.ac.uny.utbk.data.model

import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GedungModel(
    @SerializedName("gedungs")
    val gedungs: List<Gedung>
) : Parcelable

@Parcelize
data class Gedung(
    @SerializedName("nama_gedung")
    val namaGedung: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("lat_route")
    val lat_route : Double,
    @SerializedName("long_route")
    val long_route : Double,
    @SerializedName("url_gmaps")
    val url_gmaps:String,
    @SerializedName("uri_image")
    val uriImage: String,
    @SerializedName("ruangs")
    val ruangs: List<Ruang>
) : Parcelable, SearchSuggestion {
    override fun getBody(): String {
        var str = ""
        val lat = latitude
        val longi = longitude
        ruangs.forEach {
            str = str + " " + it.namaRuang
        }
        return "$namaGedung ($str)"
    }
}

@Parcelize
data class Ruang(
    @SerializedName("nama_ruangan")
    val namaRuang: String
) : Parcelable
