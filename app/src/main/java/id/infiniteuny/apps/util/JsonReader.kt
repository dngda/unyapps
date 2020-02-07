package id.infiniteuny.apps.util

import android.content.Context
import com.google.gson.Gson
import id.ac.uny.utbk.data.model.*
import id.infiniteuny.apps.R
import java.io.BufferedReader
import java.io.InputStreamReader


object JsonReader {

    fun readData(context: Context): List<Gedung> {
        var gson = Gson()
        val inStream = context.resources.openRawResource(R.raw.data_gedung)
        val bufferedReader = BufferedReader(InputStreamReader(inStream))
        val read = bufferedReader.use { it.readText() }
        var data = gson.fromJson(read, GedungModel::class.java)
        bufferedReader.close()
        return data.gedungs
    }

    fun readDataPark(context: Context): List<Park> {
        var gson = Gson()
        val inStream = context.resources.openRawResource(R.raw.data_parkir)
        val bufferedReader = BufferedReader(InputStreamReader(inStream))
        val read = bufferedReader.use { it.readText() }
        var data = gson.fromJson(read, ParkModel::class.java)
        bufferedReader.close()
        return data.parks
    }


    fun readDataMosq(context: Context): List<Mosque> {
        var gson = Gson()
        val inStream = context.resources.openRawResource(R.raw.data_mushola)
        val bufferedReader = BufferedReader(InputStreamReader(inStream))
        val read = bufferedReader.use { it.readText() }
        var data = gson.fromJson(read, MosqModel::class.java)
        bufferedReader.close()
        return data.mosque
    }

}