package id.ac.uny.utbk.data.model

import com.google.gson.annotations.SerializedName

data class Directions(
    @SerializedName("hints")
    var hints: Hints,
    @SerializedName("info")
    var info: Info,
    @SerializedName("paths")
    var paths: List<Path>
)

data class Info(
    @SerializedName("copyrights")
    var copyrights: List<String>,
    @SerializedName("took")
    var took: Int
)

data class Hints(
    @SerializedName("visited_nodes.average")
    var visitedNodesaverage: String,
    @SerializedName("visited_nodes.sum")
    var visitedNodessum: String
)

data class Path(
    @SerializedName("ascend")
    var ascend: Double,
    @SerializedName("bbox")
    var bbox: List<Double>,
    @SerializedName("descend")
    var descend: Double,
    @SerializedName("details")
    var details: Details,
    @SerializedName("distance")
    var distance: Double,
    @SerializedName("instructions")
    var instructions: List<Instruction>,
    @SerializedName("legs")
    var legs: List<Any>,
    @SerializedName("points")
    var points: Points,
    @SerializedName("points_encoded")
    var pointsEncoded: Boolean,
    @SerializedName("snapped_waypoints")
    var snappedWaypoints: SnappedWaypoints,
    @SerializedName("time")
    var time: Int,
    @SerializedName("transfers")
    var transfers: Int,
    @SerializedName("weight")
    var weight: Double
)

data class Instruction(
    @SerializedName("distance")
    var distance: Double,
    @SerializedName("heading")
    var heading: Double,
    @SerializedName("interval")
    var interval: List<Int>,
    @SerializedName("last_heading")
    var lastHeading: Double,
    @SerializedName("sign")
    var sign: Int,
    @SerializedName("street_name")
    var streetName: String,
    @SerializedName("text")
    var text: String,
    @SerializedName("time")
    var time: Int
)

data class Points(
    @SerializedName("coordinates")
    var coordinates: List<List<Double>>,
    @SerializedName("type")
    var type: String
)

class Details(
)

data class SnappedWaypoints(
    @SerializedName("coordinates")
    var coordinates: List<List<Double>>,
    @SerializedName("type")
    var type: String
)