package com.example.md3.data.model.amc

import com.example.md3.data.model.commissioning.Product
import com.example.md3.data.model.mrn.CategoryDetails
import com.example.md3.data.model.mrn.ProductDetails


data class ContractDetails(
    val status: String,
    val applicable: String,
    val contracts_old: List<Any>,
    val linked_contract_details: LinkedContractDetails?,
    val contracts: List<Contract>
)

data class Contract(
    val id: String,
    val services: List<Service>,
    val category_details: CategoryDetails,
    val products: List<Product>,
    val parts: List<Part>,
    val charges: List<Charge>,
    val created: String,
    val modified: String,
    val contract_type: String,
    val name: String,
    val duration: Int,
    val flag_onsite_service: Boolean,
    val product_age: Int,
    val total_cost: Double,
    val organisation: String,
    val category: String
)

data class Service(
    val service_type: String,
    val duration: Int,
    val free_service_count: Int,
    val start_after_days: Int
)


data class Part(
    val product_details: ProductDetails,
    val max_replacement_allowed: Int,
    val product: String
)

data class Charge(
    val name: String,
    val cost: Double
)