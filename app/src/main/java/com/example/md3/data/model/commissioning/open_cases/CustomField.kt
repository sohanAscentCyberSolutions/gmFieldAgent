package com.example.md3.data.model.commissioning.open_cases


import com.example.md3.data.model.commissioning.CustomFieldX
import com.google.gson.annotations.SerializedName

data class CustomField(
    @SerializedName("custom_field")
    val customField: CustomFieldX,
    @SerializedName("value")
    val value: String
)