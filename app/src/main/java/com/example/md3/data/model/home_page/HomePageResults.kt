package com.example.md3.data.model.home_page


import com.google.gson.annotations.SerializedName

data class HomePageResults(
    @SerializedName("icon")
    val icon: Int,
    @SerializedName("buttonTitle")
    val buttonTitle: String,
    @SerializedName("name")
    val name: String
)