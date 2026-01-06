package com.example.md3.data.model.mrn

import com.example.md3.data.model.Identifier
import com.google.gson.annotations.SerializedName




data class ProductDetails(
    @SerializedName("id") val id: String,
    @SerializedName("location_details") val locationDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("brand_details") val brandDetails: BrandDetails,
    @SerializedName("category_details") val categoryDetails: CategoryDetails,
    @SerializedName("sub_category_details") val subCategoryDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("origin_details") val originDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("region_details") val regionDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("manufacturer_details") val manufacturerDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("base_unit_of_measure_details") val baseUnitOfMeasureDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("division_details") val divisionDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("vertical_details") val verticalDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("sku_details") val skuDetails: Any?, // Change type accordingly if you have specific details
    @SerializedName("created") val created: String,
    @SerializedName("modified") val modified: String,
    @SerializedName("product_type") val productType: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("hsn") val hsn: String,
    @SerializedName("model_number") val modelNumber: String,
    @SerializedName("flag_physical") val flagPhysical: Boolean,
    @SerializedName("origin_type") val originType: String,
    @SerializedName("part_number") val partNumber: String?, // Change type accordingly if you have specific details
    @SerializedName("weight") val weight: String?, // Change type accordingly if you have specific details
    @SerializedName("dimension") val dimension: String?, // Change type accordingly if you have specific details
    @SerializedName("initial_release_date") val initialReleaseDate: String?, // Change type accordingly if you have specific details
    @SerializedName("flag_stock_alert_enabled") val flagStockAlertEnabled: Boolean,
    @SerializedName("opening_stock") val openingStock: Double,
    @SerializedName("current_stock") val currentStock: Double,
    @SerializedName("low_stock_quantity") val lowStockQuantity: Double,
    @SerializedName("max_stock_quantity") val maxStockQuantity: Double,
    @SerializedName("warranty") val warranty: Int,
    @SerializedName("inventory_type") var inventoryType: String,
    @SerializedName("description") val description: String?, // Change type accordingly if you have specific details
    @SerializedName("flag_user_manual") val flagUserManual: Boolean,
    @SerializedName("search_slug") val searchSlug: String,
    @SerializedName("organisation") val organisation: String,
    @SerializedName("location") val location: Any?, // Change type accordingly if you have specific details
    @SerializedName("master_product") val masterProduct: Any?, // Change type accordingly if you have specific details
    @SerializedName("brand") val brand: String,
    @SerializedName("category") val category: String,
    @SerializedName("sub_category") val subCategory: Any?, // Change type accordingly if you have specific details
    @SerializedName("origin") val origin: Any?, // Change type accordingly if you have specific details
    @SerializedName("region") val region: Any?, // Change type accordingly if you have specific details
    @SerializedName("default_sku") val defaultSku: Any?, // Change type accordingly if you have specific details
    @SerializedName("manufacturer") val manufacturer: Any?, // Change type accordingly if you have specific details
    @SerializedName("base_unit_of_measure") val baseUnitOfMeasure: Any?, // Change type accordingly if you have specific details
    @SerializedName("division") val division: Any?, // Change type accordingly if you have specific details
    @SerializedName("vertical") val vertical: Any?, // Change type accordingly if you have specific details
    @SerializedName("assigned_checksheet") val assignedChecksheet: Any?, // Change type accordingly if you have specific details
    @SerializedName("tags") val tags: List<Any>, // Change type accordingly if you have specific details
    var identifier: MutableList<Identifier>? = null
) {
    init {
        if (identifier == null) {
            identifier = mutableListOf(Identifier("", 0))
        }
    }
}


data class MrnDetails(
    val id: String,
    val items: List<MrnDetailsItem>,
    val created: String,
    val modified: String,
    val organisation: String,
    @SerializedName("service_request") val serviceRequest: String
)

data class MrnDetailsItem(
    @SerializedName("product_details") val productDetails: ProductDetails,
    var quantity: Int,
    var userEnteredQnty: Int = 1
)

data class MrnDetailsResponse(
    @SerializedName("mrn_details") val mrnDetails: MrnDetails
)
