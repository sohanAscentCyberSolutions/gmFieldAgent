package com.example.md3.data.network.auth

import com.example.md3.Urls
import com.example.md3.data.model.ClaimPart
import com.example.md3.data.model.ClaimPartSubmit
import com.example.md3.data.model.JourneyPath
import com.example.md3.data.model.JourneyResponse
import com.example.md3.data.model.amc.ContractDetails
import com.example.md3.data.model.breakdown.PartWiseWarrantyReponse
import com.example.md3.data.model.breakdown.WarrantyConsumedReponse
import com.example.md3.data.model.checksheet.CheckSheetResponse
import com.example.md3.data.model.checksheet.SubmitCheekSheet
import com.example.md3.data.model.commissioning.CaseRequestResponse
import com.example.md3.data.model.commissioning.CommissionCheckStatusResponse
import com.example.md3.data.model.commissioning.Details.CommissioningDetailsResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.JourneyVisitRouteResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.VisitDetailsResponse
import com.example.md3.data.model.commissioning.StartAndEndJourneyResponse
import com.example.md3.data.model.commissioning.all_cases.AllCasesResponse
import com.example.md3.data.model.commissioning.conclude.ConcludeResponse
import com.example.md3.data.model.commissioning.new_case.NewCasesResponse
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.data.model.commissioning.visit.VisitReponse
import com.example.md3.data.model.commissioning.visit.VisitResult
import com.example.md3.data.model.customer.ContractResponse
import com.example.md3.data.model.customer.CustomerProductList
import com.example.md3.data.model.customer.CustomerResponse
import com.example.md3.data.model.customer.CustomerResult
import com.example.md3.data.model.customer.LicenseResponse
import com.example.md3.data.model.home.HomeScreenKPI
import com.example.md3.data.model.mrn.BomResponse
import com.example.md3.data.model.mrn.Data
import com.example.md3.data.model.mrn.MrnDetailsResponse
import com.example.md3.data.model.mrn.MrnResponse
import com.example.md3.data.model.mrn.MrnSubmitResponse
import com.example.md3.data.model.mrn.PartsResponse
import com.example.md3.data.model.mrn.SubmitMrnResponseItem
import com.example.md3.data.model.observation.GetObservationResponse
import com.example.md3.data.model.observation.ObservationResponse
import com.example.md3.data.model.observation.ObservationResult
import com.example.md3.data.model.product.ProductDetails
import com.example.md3.data.model.rootanalysis.RootAnalysisResponse
import com.example.md3.data.model.visit.CreateVisitResponse
import com.example.md3.data.model.visit.CreateVisitSubmitResponse
import com.example.md3.data.model.visit.VisitSubmitedResponse
import com.example.md3.data.model.warranty.WarrantyInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface CommissioningApi {


    @GET(Urls.GET_NEW_CASES)
    suspend fun requestForNewCases(
        @Query("organisation_id") organisationID: String,
        @Query("assigned_engineer") assignedEngineer: String,
        @Query("page") page: Int,
        @Query("service_request__type") type: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): NewCasesResponse


    @GET(Urls.GET_NEW_CASES)
    suspend fun requestForPartyWiseWarranty(
        @Query("organisation_id") organisationID: String,
        @Query("assigned_engineer") assignedEngineer: String,
        @Query("page") page: Int,
        @Query("type") type: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): PartWiseWarrantyReponse


    @GET(Urls.GET_NEW_CASES)
    suspend fun requestForWarrantyConsumed(
        @Query("organisation_id") organisationID: String,
        @Query("assigned_engineer") assignedEngineer: String,
        @Query("page") page: Int,
        @Query("type") type: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): WarrantyConsumedReponse


    @GET(Urls.MRN_LIST)
    suspend fun requestForGetMrnList(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
        @Query("page") page: Int,
        @QueryMap queryMap: HashMap<String, Any>
    ): MrnResponse


    @GET(Urls.MRN_LIST)
    suspend fun requestForGetMrnListWithPagination(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): Data


    @POST(Urls.MRN)
    suspend fun requestForSubmitMRN(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
        @Body mrnSubmitResponse: MrnSubmitResponse
    ): SubmitMrnResponseItem


    @GET(Urls.COMMISSIONING_DETAILS)
    suspend fun requestForAllCases(
        @Query("organisation_id") organisationID: String,
        @Query("assigned_engineer") assignedEngineer: String,
        @Query("page") page: Int,
        @Query("type") type: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): AllCasesResponse


    @GET(Urls.VISIT_LIST)
    suspend fun requestForGetAllVisit(
        @Query("organisation_id") organisationID: String,
        @Query("page") page: Int,
        @QueryMap queryMap: HashMap<String, Any>
    ): VisitReponse


    @GET(Urls.GET_OPEN_CASES)
    suspend fun requestForOpenCases(
        @Query("organisation_id") organisationID: String,
        @Query("assigned_engineer") assignedEngineer: String,
        @Query("page") page: Int,
        @Query("service_request__type") type: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): VisitReponse


    @FormUrlEncoded
    @PATCH(Urls.CASE_REQUEST + "{commissioning_engineer_id}/")
    suspend fun changeCaseRequest(
        @Path("commissioning_engineer_id") commissioningEngineerId: String,
        @Query("organisation_id") organisationID: String,
        @Field("status") status: String,
        @Field("reason") reason: String,
    ): CaseRequestResponse


    @GET(Urls.VISIT_DETAILS + "{visit_id}/")
    suspend fun getVisitDetails(
        @Path("visit_id") commissioningVisitId: String,
        @Query("organisation_id") organisationID: String,
    ): GetVisitDetailsResponse


    @GET(Urls.COMMISSIONING_DETAILS + "{org_service_request_id}/")
    suspend fun getCommissioningDetails(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
    ): CommissioningDetailsResponse


    @GET(Urls.COMMISSIONING_DETAILS + "{org_service_request_id}/check-status/")
    suspend fun checkCommissioningStatus(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
    ): CommissionCheckStatusResponse


    @FormUrlEncoded
    @POST(Urls.START_JOURNEY)
    suspend fun startJourney(
        @Path("visit_id") commissioningVisitId: String,
        @Query("organisation_id") organisationID: String,
        @Field("start_time") startTime: String
    ): StartAndEndJourneyResponse


    @FormUrlEncoded
    @POST(Urls.STOP_JOURNEY)
    suspend fun endJourney(
        @Path("visit_id") commissioningVisitId: String,
        @Query("organisation_id") organisationID: String,
        @Field("end_time") startTime: String,
        @Field("journey") journeyId: String
    ): StartAndEndJourneyResponse


    @POST(Urls.JOURNEY_VISIT_ROUTE)
    suspend fun journeyVisitRoute(
        @Path("visit_id") commissioningVisitId: String,
        @Query("organisation_id") organisationID: String,
        @Body journeyVisitRouteResponse: JourneyVisitRouteResponse
    ): VisitDetailsResponse


    @FormUrlEncoded
    @POST(Urls.OBSERVATION)
    suspend fun createObservation(
        @Query("organisation_id") organisationID: String,
        @Field("visit") commissioningVisitId: String,
        @Field("observation") observation: String
    ): ObservationResponse


    @FormUrlEncoded
    @PATCH(Urls.OBSERVATION + Urls.OBSERVATION_ID_PATH)
    suspend fun editObservation(
        @Path("visit_observation_id") observationId: String,
        @Query("organisation_id") organisationID: String,
        @Field("observation") observation: String
    ): ObservationResponse


//    {{base_url}}/v{{api_version}}/commissioning/visit-observation/{{visit_observation_id}}/?organisation_id={{organisation_id}}


    @GET(Urls.OBSERVATION + Urls.OBSERVATION_ID_PATH)
    suspend fun getObservationsFromId(
        @Path("visit_observation_id") observationId: String,
        @Query("organisation_id") organisationID: String,
    ): ObservationResult


    @GET(Urls.OBSERVATION)
    suspend fun getObservation(
        @Query("organisation_id") organisationID: String,
        @Query("page") page: Int,
        @Query("visit") commissioningVisitId: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): GetObservationResponse


    @GET(Urls.CUSTOMERS)
    suspend fun getCustomers(
        @Query("organisation_id") organisationID: String,
        @Query("page") page: Int,
        @QueryMap queryMap: HashMap<String, Any>
    ): CustomerResponse



    @GET(Urls.CUSTOMERS + Urls.CUSTOMERS_ID_PATH)
    suspend fun getCustomerDetail(
        @Path("customer") customerId: String,
        @Query("organisation_id") organisationID: String,
    ): CustomerResult



    @GET(Urls.OBSERVATION)
    suspend fun getObservation(
        @Query("organisation_id") organisationID: String,
        @Query("visit") commissioningVisitId: String,
    ): GetObservationResponse


    @FormUrlEncoded
    @POST(Urls.VISIT_INPUT)
    suspend fun visitInput(
        @Path("visit_id") commissioningVisitId: String,
        @Query("organisation_id") organisationID: String,
        @Field("is_another_visit_required") isAnotherVisitRequired: Boolean,
        @Field("end_scheduled_work_date") startScheduledWorkDate: String,
        @Field("end_scheduled_work_time") endScheduledWorkTime: String,
        @Field("remark") remark: String,
        @Field("visit_commissioning_status") visitStatus: String,
        @Field("summary") visitSummary: String,
        @Field("explain_status") explainStatus: String,
        @Field("is_engineer_known") isEngineerKnow: Boolean,
        @Field("next_visit_date") nextVisitDate: String,
        @Field("next_visit_time") nextVisitTime: String,
    ): VisitSubmitedResponse


    @FormUrlEncoded
    @POST(Urls.VISIT_INPUT)
    suspend fun visitInputForBreakDown(
        @Path("visit_id") commissioningVisitId: String,
        @Query("organisation_id") organisationID: String,
        @Field("is_another_visit_required") isAnotherVisitRequired: Boolean,
        @Field("end_scheduled_work_date") startScheduledWorkDate: String,
        @Field("end_scheduled_work_time") endScheduledWorkTime: String,
        @Field("next_visit_date") nextVisitDate: String,
        @Field("next_visit_time") nextVisitTime: String,
        @Field("is_engineer_known") isEngineerKnow: Boolean,
        @Field("remark") remark: String,
        @Field("flag_physical_check_performed") flagPhysicalCheckPerformed: Boolean,
        @Field("flag_service_required") flagServiceRequired: Boolean,
        @Field("failure_category") failureCategory: String,
        @Field("fault") fault: String,
        @Field("solution_applied") solutionApplied: String,
        @Field("solution_description") solutionDescription: String,
    ): VisitSubmitedResponse







//    @POST(Urls.VISIT_INPUT)
//    @Multipart
//    suspend fun visitInputBreakDownMultipart(
//        @Path("visit_id") commissioningVisitId: String,
//        @Query("organisation_id") organisationID: String,
//        @Part("is_another_visit_required") isAnotherVisitRequired: RequestBody,
//        @Part("end_scheduled_work_date") startScheduledWorkDate: RequestBody,
//        @Part("end_scheduled_work_time") endScheduledWorkTime: RequestBody,
//        @Part("next_visit_date") nextVisitDate: RequestBody,
//        @Part("next_visit_time") nextVisitTime: RequestBody,
//        @Part("is_engineer_known") isEngineerKnow: RequestBody,
//        @Part("remark") remark: RequestBody,
//        @Part("flag_physical_check_performed") flagPhysicalCheckPerformed: RequestBody,
//        @Part("flag_service_required") flagServiceRequired: RequestBody,
//        @Part("failure_category") failureCategory: RequestBody,
//        @Part("fault") fault: RequestBody,
//        @Part("solution_applied") solutionApplied: RequestBody,
//        @Part("solution_description") solutionDescription: RequestBody,
//        @Part images: List<MultipartBody.Part>?
//    ): VisitSubmitedResponse



    //    @POST(Urls.VISIT_INPUT)
//    @Multipart
//    suspend fun visitInputNewMulti(
//        @Path("visit_id_id") commissioningVisitId: String,
//        @Query("organisation_id") organisationID: String,
//        @Part("is_another_visit_required") isAnotherVisitRequired: Boolean,
//        @Part("end_scheduled_work_date") startScheduledWorkDate: RequestBody,
//        @Part("end_scheduled_work_time") endScheduledWorkTime: RequestBody,
//        @Part("remark") remark: RequestBody,
//        @Part("commissioning_status") visitStatus: RequestBody,
//        @Part("is_engineer_known") isEngineerKnow: Boolean,
//        @Part("next_visit_date") nextVisitDate: RequestBody,
//        @Part("next_visit_time") nextVisitTime: RequestBody,
//        @Part images: List<MultipartBody.Part>?
//    ): VisitSubmitedResponse




    @GET(Urls.CHECK_SHEET)
    suspend fun getAllCheckSheet(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
    ): CheckSheetResponse


    @POST(Urls.CHECK_SHEET)
    suspend fun submitCheckSheet(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
        @Body submitCheckSheet: SubmitCheekSheet
    ): CheckSheetResponse


    @FormUrlEncoded
    @POST(Urls.START_WORK)
    suspend fun startAndEndWork(
        @Path("visit_id") commissioningVisitID: String,
        @Query("organisation_id") organisationID: String,
        @FieldMap fieldMap: HashMap<String, String>
    ): VisitResult


    @POST(Urls.CREATE_VISIT)
    suspend fun createVisit(
        @Query("organisation_id") organisationID: String,
        @Body createVisitSubmitResponse: CreateVisitSubmitResponse
    ): CreateVisitResponse


//    @FormUrlEncoded
//    @PATCH(Urls.EDIT_VISIT)
//    suspend fun editVisit(
//        @Query("organisation_id") organisationID: String,
//        @Field("is_another_visit_required") isAnotherVisitRequired: Boolean,
//        @Field("end_scheduled_work_date") startScheduledWorkDate: String,
//        @Field("end_scheduled_work_time") endScheduledWorkTime: String,
//    ): CreateVisitResponse


    @Multipart
    @POST(Urls.CONCLUDE_CASE)
    suspend fun concludeCase(
        @Path("org_service_request_id") orgCommissioningId: String,
        @Query("organisation_id") organisationID: String,
        @Part("customer_feedback") customerFeedback: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("designation") designation: RequestBody,
        @Part customer_signature: MultipartBody.Part?,
        @Part("customer_remark") customerRemark: RequestBody,
    ): ConcludeResponse


    @DELETE(Urls.CREATE_VISIT + "{visit_id}/")
    suspend fun requestForDeleteVisit(
        @Path("visit_id") invoice_id: String,
        @Query("organisation_id") organisationID: String,
    ): Response<Void>?


//    {{base_url}}/v{{api_version}}/commissioning/visit/{{visit_id_id}}/?organisation_id={{organisation_id}}

    @FormUrlEncoded
    @PATCH(Urls.CHANGE_VISIT_STATUS)
    suspend fun changeVisitStatus(
        @Path("visit_id") commissioningVisitId: String,
        @Query("organisation_id") organisationID: String,
        @Field("status") status: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
    ): VisitResult


    @FormUrlEncoded
    @POST(Urls.ROOT_ANALYSIS)
    suspend fun submitRootAnalysis(
        @Path("visit_id") commissioningVisitId: String,
        @Field("why1") why1: String,
        @Field("why2") why2: String,
        @Field("why3") why3: String,
    ): RootAnalysisResponse


    @GET(Urls.MRN_DETAILS)
    suspend fun getMrnDetails(
        @Path("org_service_request_id") orgCommissioningId: String,
        @Query("organisation_id") organisationID: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): MrnDetailsResponse



    @GET(Urls.PART_LIST)
    suspend fun getPartList(
        @Path("org_service_request_id") orgCommissioningId: String,
        @Query("organisation_id") organisationID: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): PartsResponse


    @POST(Urls.CLAIM_PART)
    suspend fun requestForSubmitClaimPart(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
        @Body claimPart: ClaimPart
    ): ClaimPartSubmit



    @GET(Urls.GET_AMC)
    suspend fun getAMCDetails(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
    ) : ContractDetails



    @GET(Urls.GET_WARRANTY)
    suspend fun getWarrantyDetails(
        @Path("org_service_request_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
    ) : WarrantyInfo



    @GET(Urls.PRODUCT_DETAILS)
    suspend fun getProductDetails(
        @Path("org_product_id") orgCommissioningID: String,
        @Query("organisation_id") organisationID: String,
    ) : ProductDetails



    @GET(Urls.GET_BOM)
    suspend fun requestForBomList(
        @Query("organisation_id") organisationID: String,
        @Query("product") orgProductID: String,
        @Query("page") page: Int,
        @QueryMap queryMap: HashMap<String, Any>
    ): BomResponse


    @GET(Urls.HOME_KPI)
    suspend fun requestForHomeKpi(
        @Query("organisation_id") organisationID: String,
        @Query("search") search: String = "",
    ) : HomeScreenKPI




    @GET(Urls.CONCLUDE_CASE)
    suspend fun getConclusionDetails(
        @Path("org_service_request_id") orgCommissioningId: String,
        @Query("organisation_id") organisationID: String,
    ): ConcludeResponse


    @GET(Urls.GET_JOURNEYS)
    suspend fun getListOfJourney(
        @Query("organisation_id") organisationID: String,
        @Query("visit") visitId: String,
    ) : JourneyResponse


    @GET(Urls.JOURNEY_PATH)
    suspend fun getJourneyPath(
        @Path("journey_id") journeyId : String,
        @Query("organisation_id") organisationID: String,
        @Query("ordering") ordering: String,
    ) : JourneyPath



    @GET(Urls.JOURNEY_PATH)
    suspend fun getContract(
        @Query("organisation_id") organisationID: String,
        @Query("customer") customer_id: String,
        @Query("page") page: Int,
    ) : ContractResponse



    @GET(Urls.GET_LICENSES)
    suspend fun getLicenses(
        @Query("organisation_id") organisationID: String,
        @Query("customer") customer_id: String,
        @Query("page") page: Int,
    ) : LicenseResponse



    @GET(Urls.GET_PRODUCTS)
    suspend fun getProduct(
        @Query("organisation_id") organisationID: String,
        @Query("customer") customer_id: String,
        @Query("page") page: Int,
    ) : CustomerProductList





}