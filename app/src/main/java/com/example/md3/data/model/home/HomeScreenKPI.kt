package com.example.md3.data.model.home


import com.google.gson.annotations.SerializedName

data class HomeScreenKPI(
    @SerializedName("breakdown_details")
    val breakdownDetails: BreakdownDetails,
    @SerializedName("combined_details")
    val combinedDetails: CombinedDetails,
    @SerializedName("commissioning_details")
    val commissioningDetails: CommissioningDetails
)