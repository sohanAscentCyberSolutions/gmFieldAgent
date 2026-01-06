package com.example.md3.data.model.mrn

data class MrnResponse(
    val next : String ,
    private val previous : String,
    val result : List<MrnResult>
)
