package com.example.md3.data.model.checksheet


import com.google.gson.annotations.SerializedName

data class CheckSheetResponse(
    @SerializedName("brand")
    val brand: List<String>,
    @SerializedName("created")
    val created: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("module")
    val module: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("organisation")
    val organisation: String,
    @SerializedName("sections")
    val sections: List<Section>,
    @SerializedName("status")
    val status: String
) {


//    fun calculateCompletionPercentage(): Double {
//        var totalFields = 0
//        var completedFields = 0
//
//        sections.forEach { section ->
//            section.subSections.forEach { subSection ->
//                subSection.fields.forEach { field ->
//                    totalFields++
//                    if (field.value != null) {
//                        completedFields++
//                    }
//                }
//            }
//        }
//
//        return if (totalFields > 0) {
//            (completedFields.toDouble() / totalFields) * 100
//        } else {
//            100.0
//        }
//    }


//    fun getAllFields(sections: List<Section>): List<Field> {
//        val allFields = mutableListOf<Field>()
//        sections.forEach { section ->
//            section.subSections.forEach { subSection ->
//                allFields.addAll(subSection.fields)
//            }
//        }
//        return allFields
//    }
//
//    fun getAllFieldLabels(): List<String> {
//        return getAllFields().map { it.labelName }
//    }


    fun countNullFields(): Int {
        var nullFieldsCount = 0
        for (section in sections) {
            for (subSection in section.subSections) {
                for (field in subSection.fields) {
                    nullFieldsCount++
                }
            }
        }
        return nullFieldsCount
    }

    fun countNonNullFields(): Int {
        var nonNullFieldsCount = 0
        for (section in sections) {
            for (subSection in section.subSections) {
                for (field in subSection.fields) {
                    if (field.value != null) {
                        nonNullFieldsCount++
                    }
                }
            }
        }
        return nonNullFieldsCount
    }


    fun calculatePercentage(nonNullCount: Double, totalCount: Double): Double {
        return if (totalCount > 0) {
            (nonNullCount / totalCount) * 100
        } else {
            100.0
        }
    }


}