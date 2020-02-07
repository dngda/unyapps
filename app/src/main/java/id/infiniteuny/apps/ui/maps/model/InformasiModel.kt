package id.ac.uny.utbk.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class InformasiModel(
    @SerializedName("informasis")
    val informasis: List<Informasi>
) : Parcelable

@Parcelize
data class Informasi(
    @SerializedName("judul")
    val title: String,
    val url: String
) : Parcelable