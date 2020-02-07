package id.ac.uny.utbk.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParkModel(
    @SerializedName("parkirs")
    val parks: List<Park>
) : Parcelable

@Parcelize
data class Park(
    @SerializedName("nama")
    val parkirName: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("long")
    val longitude: Double
) : Parcelable