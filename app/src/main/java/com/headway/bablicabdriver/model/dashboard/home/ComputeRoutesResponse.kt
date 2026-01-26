package com.headway.bablicabdriver.model.dashboard.home


data class ComputeRoutesResponse(
    val routes: List<RouteData>,
    val error: Error
)
data class Error(
    val code: Int,
    val message: String,
    val status: String
)



data class RouteData(
    val distanceMeters: Int,
    val duration: String,
    val polyline: Polyline,
    val routeToken: String
)

data class Polyline(
    val encodedPolyline: String
)


//////////////////////
//////////////////////

data class ComputeRoutesRequest(
    val computeAlternativeRoutes: Boolean = false,
    val destination: Destination = Destination(),
    val languageCode: String = "en-US",
    val origin: Origin = Origin(),
    val routeModifiers: RouteModifiers = RouteModifiers(),
    val routingPreference: String = "TRAFFIC_AWARE",
    val travelMode: String = "DRIVE",
    val units: String = "IMPERIAL"
)

data class Destination(
    val location: Location = Location()
)

data class Origin(
    val location: Location = Location()
)

data class RouteModifiers(
    val avoidFerries: Boolean = false,
    val avoidHighways: Boolean = false,
    val avoidTolls: Boolean = false
)

data class Location(
    val latLng: LatLng = LatLng(37.419734,-122.0827784)
)

data class LatLng(
    val latitude: Double = 37.419734,
    val longitude: Double = -122.0827784
)