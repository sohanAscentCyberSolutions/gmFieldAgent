package com.example.md3.data.model.commissioning.visit


import com.google.gson.annotations.SerializedName

data class Kpi(
    @SerializedName("engineers_accepted")
    val engineersAccepted: Int,
    @SerializedName("engineers_assigned")
    val engineersAssigned: Int,
    @SerializedName("engineers_pending")
    val engineersPending: Int,
    @SerializedName("engineers_rejected")
    val engineersRejected: Int
)