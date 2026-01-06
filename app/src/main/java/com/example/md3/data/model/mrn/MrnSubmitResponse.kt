package com.example.md3.data.model.mrn


data class MrnSubmitResponse(
    var service_request: String,
    val items: List<Item>
)

data class Item(
    val product: String,
    val quantity: Int
)