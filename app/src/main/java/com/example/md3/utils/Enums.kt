package com.example.md3.utils



enum class CASETYPE(val property: String){
    BREAKDOWN("breakdown"),
    COMMISSIONING("commissioning")
}


enum class NOTIFICATION(val notificationType : String){
    BREAKDOWN_NEW_CASE("breakdown_new_case"),
    BREAKDOWN_UPCOMING_VISIT("breakdown_upcoming_visit"),
    COMMISSIONING_NEW_CASE("commissioning_new_case"),
    COMMISSIONING_UPCOMING_VISIT("commissioning_upcoming_visit"),
}