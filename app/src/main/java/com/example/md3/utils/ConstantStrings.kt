package com.codenicely.gimbook.saudi.einvoice.utils

import com.example.md3.utils.TabType

object ConstantStrings {

    val accepted: String  = "Accepted"
    val rejected  = "Rejected"

    val TAB_TITLES = listOf("New", "Upcoming visits", "All")
    val TAB_TYPES = listOf(TabType.NEW, TabType.UPCOMING_VISITS, TabType.ALL_CASES)
    val VISIT_TAB_TYPES = listOf(TabType.MY_VISITS, TabType.PAST_VISIT)
    val WARRANTY_TAB_TYPES = listOf(TabType.PART_WISE_WARRANTY , TabType.WARRANTY_CONSUMED)


    val TAB_TITLES_FOR_CUSTOMER_DETAILS  = listOf("General Details", "Contracts")





    // Notifications Constant

      const val NOTIFICATION_TITLE = "notification_title"
      const val DATA_MAP = "data_map"
      const val NOTIFICATION_MESSAGE = "notification_message"
      const val NOTIFICATION_TYPE = "notification_type"
      const val NOTIFICATION_CASE_ID = "case_id"
      const val NOTIFICATION_CASE_FORMATTED_ID = "formatted_case_id"






}