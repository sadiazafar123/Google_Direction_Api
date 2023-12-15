package com.example.googledirectionapiapplication.models

class PostData {
    data class DrawRouteDirection(
        val routes: ArrayList<RouteClass>
    )

    data class RouteClass(
        val bounds: BoundClass,
        val legs: ArrayList<LegsClass>,
        val overview_polyline: OverViewPolylineClass
    )

    data class BoundClass(
        val northeast: LatLngClass,
        val southwest: LatLngClass
    )

    data class LegsClass(
        val distance: DistanceClass,
        val duration: DurationClass,
        val end_address: String,
        val end_location: LatLngClass,
        val start_address: String,
        val start_location: LatLngClass,
        val steps: ArrayList<StepsClass>
    )

    data class StepsClass(
        val distance: DistanceClass,
        val duration: DurationClass,
        val end_location: LatLngClass
    )

    data class LatLngClass(
        val latitude: Double,
        val l: Double

    )

    data class DistanceClass(
        val text: String,
        val value: Int
    )

    data class DurationClass(
        val test: String,
        val value: Int
    )

    data class OverViewPolylineClass(
        val points: String
    )


}