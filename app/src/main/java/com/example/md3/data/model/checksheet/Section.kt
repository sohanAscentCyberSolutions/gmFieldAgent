package com.example.md3.data.model.checksheet


import com.google.gson.annotations.SerializedName

data class Section(
    @SerializedName("id")
    val id: String,
    @SerializedName("section")
    val section: String,
    @SerializedName("sub_sections")
    val subSections: List<SubSection>
) {





    fun countNullFields(): Int {
        var nullFieldsCount = 0
        for (section in subSections) {
            for (field in section.fields) {

                nullFieldsCount++

            }

        }
        return nullFieldsCount
    }

    fun countNonNullFields(): Int {
        var nonNullFieldsCount = 0
        for (section in subSections) {
            for (field in section.fields) {
                if (field.value != null) {
                    nonNullFieldsCount++
                }
            }
        }
        return nonNullFieldsCount
    }


    fun calculatePercentage(value: Double, total: Double): Double {
        return if (total > 0) {
            (value / total) * 100
        } else {
            100.0
        }
    }

}