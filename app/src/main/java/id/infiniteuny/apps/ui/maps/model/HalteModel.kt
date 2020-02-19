package id.ac.uny.utbk.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HalteModel(
    @SerializedName("haltes")
    val halte: List<Halte>
) : Parcelable

@Parcelize
data class Halte(
    @SerializedName("nama")
    val halteName: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("long")
    val longitude: Double
) : Parcelable