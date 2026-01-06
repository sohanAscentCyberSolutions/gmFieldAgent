package com.example.md3.data.model.commissioning.JourneyVisitRoute


import com.google.gson.annotations.SerializedName

data class JourneyVisitRouteResponse(
    @SerializedName("journey")
    val journey: String,
    @SerializedName("route")
    val route: List<Route>
)