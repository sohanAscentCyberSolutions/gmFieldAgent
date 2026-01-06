package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class TES(
    @SerializedName("applicable")
    val applicable: String,
    @SerializedName("contracts")
    val contracts: List<Any>,
    @SerializedName("linked_contract_details")
    val linkedContractDetails: LinkedContractDetails,
    @SerializedName("status")
    val status: String
)