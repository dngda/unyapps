package id.ac.uny.utbk.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MosqModel(
    @SerializedName("musholas")
    val mosque: List<Mosque>
) : Parcelable

@Parcelize
data class Mosque(
    @SerializedName("nama")
    val mosqueName: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("long")
    val longitude: Double
) : Parcelable