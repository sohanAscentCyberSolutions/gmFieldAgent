package com.example.md3


object Urls {

    val BASE_URL = BuildConfig.BASE_URL
    const val URL_PREFIX = "v1/"




    // Auth
    const val GET_USER_PROFILE =  URL_PREFIX + "organisation/user/profile/"
    const val REFRESH_TOKEN = URL_PREFIX + "user/auth/token/refresh/"
    const val LOGIN_WITH_EMAIL =  URL_PREFIX +"user/auth/email-login/"
    const val LOGIN_WITH_OTP = URL_PREFIX + "user/auth/get-otp/"
    const val VERIFY_OTP = URL_PREFIX + "user/auth/verify/"



    // case

    const val GET_NEW_CASES =  URL_PREFIX + "service-request/engineer/"
    const val GET_CLOSE_CASES = URL_PREFIX + "service-request/visit/"
    const val GET_OPEN_CASES = URL_PREFIX + "service-request/visit/"



    const val CASE_REQUEST = URL_PREFIX + "service-request/engineer/"



    const val VISIT_DETAILS =  URL_PREFIX + "service-request/visit/"
    const val VISIT_LIST =  URL_PREFIX + "service-request/visit/"
    const val VISIT_INPUT = URL_PREFIX + "service-request/visit/{visit_id}/complete/"

    const val COMMISSIONING_DETAILS =  URL_PREFIX + "service-request/"


    const val START_JOURNEY =  URL_PREFIX + "service-request/visit/" +  "{visit_id}/" + "start-journey/"
    const val STOP_JOURNEY =  URL_PREFIX + "service-request/visit/" +  "{visit_id}/" + "stop-journey/"


    const val JOURNEY_VISIT_ROUTE  =  URL_PREFIX + "service-request/visit/" +  "{visit_id}/" + "record/route/"


    const val OBSERVATION  =  URL_PREFIX + "service-request/visit-observation/"
    const val CUSTOMERS  =  URL_PREFIX + "customer/"
    const val CUSTOMERS_ID_PATH  =   "{customer}/"


    const val OBSERVATION_ID_PATH  =   "{visit_observation_id}/"


    const val CHECK_SHEET = URL_PREFIX  + "service-request/{org_service_request_id}/check-sheet/"


    const val START_WORK = URL_PREFIX  + "service-request/visit/{visit_id}/update-work-time/"




    const val CREATE_VISIT = URL_PREFIX  + "service-request/visit/"
    const val EDIT_VISIT = URL_PREFIX  + "service-request/visit/{visit_id}"

    const val CONCLUDE_CASE = URL_PREFIX  + "service-request/{org_service_request_id}/conclusion/submit/"


    const val GET_CHECK_SHEET =  URL_PREFIX + "service-request/{org_service_request_id}/check-sheet"


    const val CHANGE_VISIT_STATUS =  URL_PREFIX + "service-request/visit/{visit_id}/"


    const val MRN_LIST =  URL_PREFIX + "service-request/{org_service_request_id}/part-list/"

    const val MRN =  URL_PREFIX + "service-request/{org_service_request_id}/mrn/"

    const val MRN_DETAILS =  URL_PREFIX + "service-request/{org_service_request_id}/mrn/"

    const val PART_LIST =  URL_PREFIX + "service-request/{org_service_request_id}/part-list/"

    const val CLAIM_PART = URL_PREFIX + "service-request/{org_service_request_id}/claim-parts/"

    const val GET_AMC =  URL_PREFIX + "service-request/{org_service_request_id}/amc/"

    const val GET_WARRANTY =  URL_PREFIX + "service-request/{org_service_request_id}/warranty/"

    const val PRODUCT_DETAILS = URL_PREFIX + "organisation/product/{org_product_id}/"


    const val ROOT_ANALYSIS =  URL_PREFIX + "service-request/visit/{visit_id}/"
    const val GET_BOM = URL_PREFIX + "organisation/product/bom/"


    const val HOME_KPI = URL_PREFIX + "/organisation/user/case-kpi/"


    const val GET_JOURNEYS =  URL_PREFIX + "service-request/visit/journey/"
    const val JOURNEY_PATH = URL_PREFIX + "service-request/visit/journey/{journey_id}/path/"
    const val GET_LICENSES = URL_PREFIX + "customer/document/"
    const val GET_PRODUCTS = URL_PREFIX + "customer/products/"





}