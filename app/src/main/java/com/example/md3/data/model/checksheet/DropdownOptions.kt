package com.example.md3.data.model.checksheet


import com.google.gson.annotations.SerializedName

data class DropdownOptions(
    @SerializedName("list_options")
    val listOptions: List<String>
)