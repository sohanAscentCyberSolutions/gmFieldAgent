package com.example.md3.data.model.breakdown

data class PartWiseWarrantyResult(
   val id : String,
   val partName : String,
   val partNumber: String,
   val isUnderWarranty : Boolean,
   val warrantyStatusStartDate : String,
   val warrantyStatusEndDate : String,
)