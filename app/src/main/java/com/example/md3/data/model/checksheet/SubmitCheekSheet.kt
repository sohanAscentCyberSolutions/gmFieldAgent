package com.example.md3.data.model.checksheet

import com.google.gson.annotations.SerializedName


data class SubmitCheekSheet (
    @SerializedName("fields")
    val fields: List<Field>
)




