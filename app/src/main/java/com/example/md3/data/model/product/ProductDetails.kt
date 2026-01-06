package com.example.md3.data.model.product


import com.google.gson.annotations.SerializedName

data class ProductDetails(
    @SerializedName("assigned_checksheet")
    val assignedChecksheet: Any,
    @SerializedName("base_unit_of_measure")
    val baseUnitOfMeasure: Any,
    @SerializedName("base_unit_of_measure_details")
    val baseUnitOfMeasureDetails: Any,
    @SerializedName("brand")
    val brand: String,
    @SerializedName("brand_details")
    val brandDetails: BrandDetails,
    @SerializedName("category")
    val category: String,
    @SerializedName("category_details")
    val categoryDetails: CategoryDetails,
    @SerializedName("created")
    val created: String,
    @SerializedName("current_stock")
    val currentStock: Double,
    @SerializedName("default_sku")
    val defaultSku: String,
    @SerializedName("description")
    val description: Any,
    @SerializedName("dimension")
    val dimension: Any,
    @SerializedName("division")
    val division: Any,
    @SerializedName("division_details")
    val divisionDetails: Any,
    @SerializedName("flag_physical")
    val flagPhysical: Boolean,
    @SerializedName("flag_stock_alert_enabled")
    val flagStockAlertEnabled: Boolean,
    @SerializedName("flag_user_manual")
    val flagUserManual: Boolean,
    @SerializedName("hsn")
    val hsn: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("initial_release_date")
    val initialReleaseDate: String,
    @SerializedName("inventory_type")
    val inventoryType: String,
    @SerializedName("location")
    val location: Any,
    @SerializedName("location_details")
    val locationDetails: Any,
    @SerializedName("low_stock_quantity")
    val lowStockQuantity: Double,
    @SerializedName("manufacturer")
    val manufacturer: String,
    @SerializedName("manufacturer_details")
    val manufacturerDetails: Any,
    @SerializedName("master_product")
    val masterProduct: Any,
    @SerializedName("max_stock_quantity")
    val maxStockQuantity: Double,
    @SerializedName("model_number")
    val modelNumber: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("opening_stock")
    val openingStock: Double,
    @SerializedName("organisation")
    val organisation: String,
    @SerializedName("origin")
    val origin: Any,
    @SerializedName("origin_details")
    val originDetails: Any,
    @SerializedName("origin_type")
    val originType: String,
    @SerializedName("part_number")
    val partNumber: String,
    @SerializedName("product_type")
    val productType: String,
    @SerializedName("region")
    val region: Any,
    @SerializedName("region_details")
    val regionDetails: Any,
    @SerializedName("search_slug")
    val searchSlug: String,
    @SerializedName("sku_details")
    val skuDetails: Any,
    @SerializedName("sub_category")
    val subCategory: Any,
    @SerializedName("sub_category_details")
    val subCategoryDetails: Any,
    @SerializedName("tags")
    val tags: List<Any>,
    @SerializedName("vertical")
    val vertical: Any,
    @SerializedName("vertical_details")
    val verticalDetails: Any,
    @SerializedName("warranty")
    val warranty: Int,
    @SerializedName("weight")
    val weight: String
){

    fun toListOfTitleAndValue(): List<ProductDetailTitleAndValue> {
        val list = mutableListOf<ProductDetailTitleAndValue>()
        list.add(ProductDetailTitleAndValue("Product Name", name ?: "----"))
        list.add(ProductDetailTitleAndValue("Initial Release Date", initialReleaseDate ?: "----"))
        list.add(ProductDetailTitleAndValue("Brand Name", brandDetails.name ?: "----"))
        list.add(ProductDetailTitleAndValue("Manufacturer Name", manufacturer ?: "----"))
        list.add(ProductDetailTitleAndValue("Category", category ?: "----"))
        list.add(ProductDetailTitleAndValue("Product Type", productType ?: "----"))
        list.add(ProductDetailTitleAndValue("Inventory Type", inventoryType ?: "----"))
        list.add(ProductDetailTitleAndValue("Is Physical", if (flagPhysical) "Yes" else "No"))
        list.add(ProductDetailTitleAndValue("Model Number", modelNumber ?: "----"))
        list.add(ProductDetailTitleAndValue("HSN Number", hsn ?: "----"))
        list.add(ProductDetailTitleAndValue("Default SkU", defaultSku ?: "----"))
        list.add(ProductDetailTitleAndValue("Part Number", partNumber ?: "----"))
        list.add(ProductDetailTitleAndValue("Weight", weight ?: "----"))
        list.add(ProductDetailTitleAndValue("Dimensions", dimension?.toString() ?: "----"))
        list.add(ProductDetailTitleAndValue("Division", division?.toString() ?: "----"))
        list.add(ProductDetailTitleAndValue("Vertical", vertical?.toString() ?: "----"))
        list.add(ProductDetailTitleAndValue("Bill Of Material", "View BOM"))

        return list
    }



}


data class ProductDetailTitleAndValue(
    val title: String,
    val value: String
)