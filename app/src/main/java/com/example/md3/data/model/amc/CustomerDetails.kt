package com.example.md3.data.model.amc


import com.google.gson.annotations.SerializedName

data class CustomerDetails(
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("addresses")
    val addresses: List<Addresse>,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("communication_mode")
    val communicationMode: Any,
    @SerializedName("contact_person")
    val contactPerson: List<Any>,
    @SerializedName("created")
    val created: String,
    @SerializedName("credit_limit")
    val creditLimit: Int,
    @SerializedName("currency")
    val currency: Any,
    @SerializedName("custom_fields")
    val customFields: List<Any>,
    @SerializedName("customer_since")
    val customerSince: String,
    @SerializedName("customer_sub_type")
    val customerSubType: String,
    @SerializedName("customer_type")
    val customerType: Any,
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("emails")
    val emails: List<String>,
    @SerializedName("facebook_link")
    val facebookLink: String,
    @SerializedName("flag_main_account")
    val flagMainAccount: Boolean,
    @SerializedName("flag_send_marketing_content")
    val flagSendMarketingContent: Boolean,
    @SerializedName("group")
    val group: Any,
    @SerializedName("gstin")
    val gstin: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("incorporation_date")
    val incorporationDate: String,
    @SerializedName("industry_size")
    val industrySize: String,
    @SerializedName("industry_type")
    val industryType: Any,
    @SerializedName("instagram_link")
    val instagramLink: String,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("linkedin_link")
    val linkedinLink: String,
    @SerializedName("modified")
    val modified: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("notes")
    val notes: String,
    @SerializedName("organisation")
    val organisation: String,
    @SerializedName("pan")
    val pan: String,
    @SerializedName("parent")
    val parent: Any,
    @SerializedName("payment_term")
    val paymentTerm: Any,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("phone_code")
    val phoneCode: String,
    @SerializedName("preferred_language")
    val preferredLanguage: Any,
    @SerializedName("referral_source")
    val referralSource: Any,
    @SerializedName("revenue")
    val revenue: Int,
    @SerializedName("sales_location")
    val salesLocation: Any,
    @SerializedName("sales_representatives")
    val salesRepresentatives: List<Any>,
    @SerializedName("service_location")
    val serviceLocation: Any,
    @SerializedName("service_representatives")
    val serviceRepresentatives: List<Any>,
    @SerializedName("signal_link")
    val signalLink: String,
    @SerializedName("tags")
    val tags: List<Any>,
    @SerializedName("telegram_link")
    val telegramLink: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("x_link")
    val xLink: String,
    @SerializedName("youtube_link")
    val youtubeLink: String
)