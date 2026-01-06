package com.example.md3.data.model.mrn

import com.example.md3.data.model.Identifier
import com.google.gson.annotations.SerializedName

data class MrnResult(
    val name : String,
    val id : String,
    val partName : String,
    val productSerial  :  String,
    val isOutOfWarranty : Boolean,
    var userEnteredQnty : Int? = 0
)




data class BrandDetails(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class CategoryDetails(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String
)

data class WarrantyDetails(
    @SerializedName("status") val status: String,
    @SerializedName("warranty_till") val warrantyTill: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("coverage_months") val coverageMonths: Int
)

data class BomItemDetails(
    @SerializedName("id") val id: String,
    @SerializedName("location_details") val locationDetails: Map<String, Any>? = null,
    @SerializedName("brand_details") val brandDetails: BrandDetails,
    @SerializedName("category_details") val categoryDetails: CategoryDetails,
    @SerializedName("sub_category_details") val subCategoryDetails: Map<String, Any>? = null,
    @SerializedName("origin_details") val originDetails: Map<String, Any>? = null,
    @SerializedName("region_details") val regionDetails: Map<String, Any>? = null,
    @SerializedName("manufacturer_details") val manufacturerDetails: Map<String, Any>? = null,
    @SerializedName("base_unit_of_measure_details") val baseUnitOfMeasureDetails: Map<String, Any>? = null,
    @SerializedName("division_details") val divisionDetails: Map<String, Any>? = null,
    @SerializedName("vertical_details") val verticalDetails: Map<String, Any>? = null,
    @SerializedName("sku_details") val skuDetails: Map<String, Any>? = null,
    @SerializedName("created") val created: String,
    @SerializedName("modified") val modified: String,
    @SerializedName("product_type") val productType: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("hsn") val hsn: String,
    @SerializedName("model_number") val modelNumber: String,
    @SerializedName("flag_physical") val flagPhysical: Boolean,
    @SerializedName("origin_type") val originType: String,
    @SerializedName("part_number") val partNumber: String? = null,
    @SerializedName("weight") val weight: Double? = null,
    @SerializedName("dimension") val dimension: Double? = null,
    @SerializedName("initial_release_date") val initialReleaseDate: String? = null,
    @SerializedName("flag_stock_alert_enabled") val flagStockAlertEnabled: Boolean,
    @SerializedName("opening_stock") val openingStock: Double,
    @SerializedName("current_stock") val currentStock: Double,
    @SerializedName("low_stock_quantity") val lowStockQuantity: Double,
    @SerializedName("max_stock_quantity") val maxStockQuantity: Double,
    @SerializedName("warranty") val warranty: Int,
    @SerializedName("inventory_type") val inventoryType: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("flag_user_manual") val flagUserManual: Boolean,
    @SerializedName("search_slug") val searchSlug: String,
    @SerializedName("organisation") val organisation: String,
    @SerializedName("location") val location: Map<String, Any>? = null,
    @SerializedName("master_product") val masterProduct: Any? = null,
    @SerializedName("brand") val brand: String,
    @SerializedName("category") val category: String,
    @SerializedName("sub_category") val subCategory: String? = null,
    @SerializedName("origin") val origin: String? = null,
    @SerializedName("region") val region: String? = null,
    @SerializedName("default_sku") val defaultSku: String? = null,
    @SerializedName("manufacturer") val manufacturer: String? = null,
    @SerializedName("base_unit_of_measure") val baseUnitOfMeasure: String? = null,
    @SerializedName("division") val division: String? = null,
    @SerializedName("vertical") val vertical: String? = null,
    @SerializedName("assigned_checksheet") val assignedChecksheet: String? = null,
    @SerializedName("tags") val tags: List<Any>,
    var identifier: MutableList<Identifier>? = null
){

    fun toProductDetails(): ProductDetails {
        return ProductDetails(
            id,
            locationDetails,
            brandDetails,
            categoryDetails,
            subCategoryDetails,
            originDetails,
            regionDetails,
            manufacturerDetails,
            baseUnitOfMeasureDetails,
            divisionDetails,
            verticalDetails,
            skuDetails,
            created,
            modified,
            productType,
            name,
            hsn,
            modelNumber,
            flagPhysical,
            originType,
            partNumber,
            weight.toString(),
            dimension.toString(),
            initialReleaseDate,
            flagStockAlertEnabled,
            openingStock,
            currentStock,
            lowStockQuantity,
            maxStockQuantity,
            warranty,
            inventoryType,
            description,
            flagUserManual,
            searchSlug,
            organisation,
            location,
            masterProduct,
            brand,
            category,
            subCategory,
            origin,
            region,
            defaultSku,
            manufacturer,
            baseUnitOfMeasure,
            division,
            vertical,
            assignedChecksheet,
            tags,
            identifier
        )
    }

}

data class MrnItem(
    @SerializedName("bom_item_details") val bomItemDetails: BomItemDetails,
    @SerializedName("warranty_details") val warrantyDetails: WarrantyDetails,
    @SerializedName("quantity") var quantity: Int,
    var userEnteredQnty: Int = 1
)

data class Data(
    @SerializedName("items") val items: List<MrnItem>
)


