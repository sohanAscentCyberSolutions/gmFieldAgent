package com.example.md3.data.source.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.md3.data.model.checksheet.CheckSheetResponse
import com.example.md3.data.model.checksheet.SubmitCheekSheet
import com.example.md3.data.model.commissioning.CaseRequestResponse
import com.example.md3.data.model.commissioning.Details.CommissioningDetailsResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.JourneyVisitRouteResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.VisitDetailsResponse
import com.example.md3.data.model.commissioning.ServiceRequestDetails
import com.example.md3.data.model.commissioning.StartAndEndJourneyResponse
import com.example.md3.data.model.commissioning.conclude.ConcludeResponse
import com.example.md3.data.model.commissioning.new_case.NewCasesResult
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.data.model.commissioning.visit.VisitResult
import com.example.md3.data.model.mrn.MrnResult
import com.example.md3.data.model.observation.GetObservationResponse
import com.example.md3.data.model.observation.ObservationResponse
import com.example.md3.data.model.observation.ObservationResult
import com.example.md3.data.model.visit.CreateVisitResponse
import com.example.md3.data.model.visit.CreateVisitSubmitResponse
import com.example.md3.data.model.visit.VisitSubmitedResponse
import com.example.md3.data.network.auth.CommissioningApi
import com.example.md3.data.preferences.SharedPrefs
import com.example.md3.utils.network.Resource
import com.example.md3.utils.network.ResponseHandler
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.java.KoinJavaComponent

class BreakdownRepo(
    private val api: CommissioningApi,
    private val responseHandler: ResponseHandler
) {
    private val sharedPrefs: SharedPrefs by KoinJavaComponent.inject(SharedPrefs::class.java)
    private val TAG = "CommissioningRepo"



    fun getCommissioningList(
        queryMap: HashMap<String, Any>,
    ): LiveData<PagingData<NewCasesResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100
            ),
            pagingSourceFactory = {
                CommissioningPagingSource(
                    sharedPrefs.organisationId,
                    sharedPrefs.organisationUserId,
                    api,
                    responseHandler,
                    queryMap
                )
            },
        ).liveData
    }



    fun getSuppliers(
        queryMap: HashMap<String, Any>,
    ): LiveData<PagingData<MrnResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100
            ),
            pagingSourceFactory = {
                SupplerPagingSource(
                    sharedPrefs.organisationId,
                    sharedPrefs.organisationUserId,
                    api,
                    responseHandler,
                    queryMap
                )
            },
        ).liveData
    }




    fun getClosedCases(
        queryMap: HashMap<String, Any>,
    ): LiveData<PagingData<ServiceRequestDetails>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100
            ),
            pagingSourceFactory = {
                ClosedCasePagingSource(
                    sharedPrefs.organisationId,
                    sharedPrefs.organisationUserId,
                    api,
                    responseHandler,
                    queryMap
                )
            },
        ).liveData
    }


    fun getAllVisits(
        queryMap: HashMap<String, Any>,
    ): LiveData<PagingData<VisitResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100
            ),
            pagingSourceFactory = {
                VisitPagingSource(
                    sharedPrefs.organisationId, api, responseHandler, queryMap
                )
            },
        ).liveData
    }

    fun getMyVisits(
        queryMap: HashMap<String, Any>,
    ): LiveData<PagingData<VisitResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100
            ),
            pagingSourceFactory = {
                VisitPagingSource(
                    sharedPrefs.organisationId,
                    api,
                    responseHandler,
                    queryMap
//                    queryMap = hashMapOf("assigned_engineer" to sharedPrefs.organisationUserId)
                )
            },
        ).liveData
    }


    fun getOpenCases(
        queryMap: HashMap<String, Any>,
    ): LiveData<PagingData<VisitResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100
            ),
            pagingSourceFactory = {
                OpenCasePagingSource(
                    sharedPrefs.organisationId,
                    sharedPrefs.organisationUserId,
                    api,
                    responseHandler,
                    queryMap
                )
            },
        ).liveData
    }


    fun getAllObservations(
        commissioningVisitId: String,
        queryMap: HashMap<String, Any>,
    ): LiveData<PagingData<ObservationResult>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                maxSize = 100
            ),
            pagingSourceFactory = {
                ObservationPagingSource(
                    sharedPrefs.organisationId, commissioningVisitId, api, responseHandler, queryMap
                )
            },
        ).liveData
    }


    suspend fun changeCaseRequest(
        status: String,
        reason: String,
        commissioningEngineerId: String
    ): Resource<CaseRequestResponse> {
        return try {
            responseHandler.handleSuccess(
                api.changeCaseRequest(
                    commissioningEngineerId,
                    sharedPrefs.organisationId,
                    status,
                    reason,
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun getVisitDetails(
        commissioningVisitId: String
    ): Resource<GetVisitDetailsResponse> {
        return try {
            responseHandler.handleSuccess(
                api.getVisitDetails(commissioningVisitId, sharedPrefs.organisationId)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun getCommissioningDetails(
        orgCommissioningID: String
    ): Resource<CommissioningDetailsResponse> {
        return try {
            responseHandler.handleSuccess(
                api.getCommissioningDetails(orgCommissioningID, sharedPrefs.organisationId)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun startJourney(
        commissioningVisitId: String,
        startTime: String
    ): Resource<StartAndEndJourneyResponse> {
        return try {
            responseHandler.handleSuccess(
                api.startJourney(
                    commissioningVisitId,
                    sharedPrefs.organisationId,
                    startTime
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun endJourney(
        commissioningVisitId: String,
        endTime: String,
        journeyId: String
    ): Resource<StartAndEndJourneyResponse> {
        return try {
            responseHandler.handleSuccess(
                api.endJourney(
                    commissioningVisitId,
                    sharedPrefs.organisationId,
                    endTime,
                    journeyId
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun visitRouteJourney(
        commissioningVisitId: String,
        journeyVisitRouteResponse: JourneyVisitRouteResponse
    ): Resource<VisitDetailsResponse> {
        return try {
            responseHandler.handleSuccess(
                api.journeyVisitRoute(
                    commissioningVisitId,
                    sharedPrefs.organisationId,
                    journeyVisitRouteResponse
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun createVisitObservation(
        commissioningVisitId: String,
        observation: String
    ): Resource<ObservationResponse> {
        return try {
            responseHandler.handleSuccess(
                api.createObservation(
                    sharedPrefs.organisationId,
                    commissioningVisitId,
                    observation
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }




    suspend fun editVisitObservation(
        observationId: String,
        observation: String
    ): Resource<ObservationResponse> {
        return try {
            responseHandler.handleSuccess(
                api.editObservation(
                    observationId,
                    sharedPrefs.organisationId,
                    observation
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }



    suspend fun getObservationById(observationId: String) : Resource<ObservationResult>?{
        return try {
            responseHandler.handleSuccess(
                api.getObservationsFromId(
                    observationId,
                    sharedPrefs.organisationId
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }



    suspend fun visitInput(
        commissioningVisitId: String,
        isAnotherVisitRequired: Boolean,
        startScheduledWorkDate: String,
        endScheduledWorkTime: String,
        remark: String,
        visitStatus: String,
        visitSummary: String,
        explainStatus: String,
        isEngineerKnow: Boolean,
        nextVisitDate: String,
        nextVisitTime: String,
        imagesList: List<MultipartBody.Part>?
    ): Resource<VisitSubmitedResponse> {
//        return try {
//            val startScheduledWorkDateRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), startScheduledWorkDate)
//            val endScheduledWorkTimeRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), endScheduledWorkTime)
//            val remarkRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), remark)
//            val visitStatus = RequestBody.create("text/plain".toMediaTypeOrNull(), visitStatus)
//            val nextVisitDateRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), nextVisitDate)
//            val nextVisitTimeRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), nextVisitTime)
//
//            responseHandler.handleSuccess(
//                api.visitInputNew(
//                    commissioningVisitId,
//                    sharedPrefs.organisationId,
//                    isAnotherVisitRequired,
//                    startScheduledWorkDateRequestBody,
//                    endScheduledWorkTimeRequestBody,
//                    remarkRequestBody,
//                    visitStatus,
//                    isEngineerKnow,
//                    nextVisitDateRequestBody,
//                    nextVisitTimeRequestBody,
//                    imagesList
//                )
//            )
//        } catch (e: Exception) {
//            responseHandler.handleException(e)
//        }


        return try {
            responseHandler.handleSuccess(
                api.visitInput(
                    commissioningVisitId,
                    sharedPrefs.organisationId,
                    isAnotherVisitRequired,
                    startScheduledWorkDate,
                    endScheduledWorkTime,
                    remark,
                    visitStatus,
                    visitSummary,
                    explainStatus,
                    isEngineerKnow,
                    nextVisitDate,
                    nextVisitTime
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }

    }


    suspend fun getAllCheckSheet(orgCommissioningID: String): Resource<CheckSheetResponse>? {
        return try {
            responseHandler.handleSuccess(
                api.getAllCheckSheet(
                    orgCommissioningID,
                    sharedPrefs.organisationId
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun submitCheckSheet(submitCheekSheet: SubmitCheekSheet ,orgCommissioningID: String): Resource<CheckSheetResponse>? {
        return try {
            responseHandler.handleSuccess(
                api.submitCheckSheet(
                    orgCommissioningID,
                    sharedPrefs.organisationId,
                    submitCheekSheet
                )
            )
        } catch (e: Exception) {
            Log.d(TAG, "submitCheckSheet: " + e.localizedMessage)
            responseHandler.handleException(e)
        }
    }


    suspend fun startAndEndWork(
        commissioningVisitID: String,
//        orgCommissioningID: String,
        startTime: String?,
        endTime: String?,
    ): Resource<VisitResult>? {
        return try {
            val fieldMap = hashMapOf<String, String>()
            if (startTime?.isNotEmpty() == true) {
                fieldMap["start_time"] = startTime
            }
            if (endTime?.isNotEmpty() == true) {
                fieldMap["end_time"] = endTime
            }

            responseHandler.handleSuccess(
                api.startAndEndWork(
                    commissioningVisitID,
//                    orgCommissioningID,
                    sharedPrefs.organisationId,
                    fieldMap
                )
            )
        } catch (e: Exception) {
            Log.d(TAG, "startAndEndWork: " + e.localizedMessage)
            responseHandler.handleException(
                e
            )
        }
    }


    suspend fun createVisit(createVisitSubmitResponse: CreateVisitSubmitResponse): Resource<CreateVisitResponse>? {
        return try {
            responseHandler.handleSuccess(
                api.createVisit(sharedPrefs.organisationId, createVisitSubmitResponse)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun getAllObservation(
        commissioningVisitId: String,
    ): Resource<GetObservationResponse>? {
        return try {
            responseHandler.handleSuccess(
                api.getObservation(sharedPrefs.organisationId, commissioningVisitId)
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }








    suspend fun concludeCase(
        org_commissioning_id: String,
        customer_feedback: String,
        name: String,
        email: String,
        phone: String,
        designation : String,
        customer_signature : MultipartBody.Part,
        customer_remark : String
    ): Resource<ConcludeResponse> {
        return try {
            responseHandler.handleSuccess(
                api.concludeCase(
                    org_commissioning_id,
                    sharedPrefs.organisationId,
                    customer_feedback.toRequestBody("text/plain".toMediaTypeOrNull()),
                    name.toRequestBody("text/plain".toMediaTypeOrNull()),
                    email.toRequestBody("text/plain".toMediaTypeOrNull()),
                    phone.toRequestBody("text/plain".toMediaTypeOrNull()),
                    designation.toRequestBody("text/plain".toMediaTypeOrNull()),
                    customer_signature,
                    customer_remark.toRequestBody("text/plain".toMediaTypeOrNull())
                )
            )
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }

    }

}