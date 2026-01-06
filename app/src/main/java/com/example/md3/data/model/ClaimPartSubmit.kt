package com.example.md3.data.model

import com.google.gson.annotations.SerializedName



data class ClaimPart(
    @SerializedName("parts") val parts: List<ClaimPartSubmit>
)



data class ClaimPartSubmit(
    @SerializedName("product") val productId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("identifiers") val identifiers: List<Identifier>
)

data class Identifier(
    @SerializedName("identifier") var identifier: String,
    @SerializedName("quantity") var quantity: Int
)