package com.example.md3.view.breakdown

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.md3.data.model.checksheet.CheckSheetResponse
import com.example.md3.data.model.checksheet.SubmitCheekSheet
import com.example.md3.data.model.commissioning.CaseRequestResponse
import com.example.md3.data.model.commissioning.Details.CommissioningDetailsResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.JourneyVisitRouteResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.VisitDetailsResponse
import com.example.md3.data.model.commissioning.StartAndEndJourneyResponse
import com.example.md3.data.model.commissioning.conclude.ConcludeResponse
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.data.model.commissioning.visit.VisitResult
import com.example.md3.data.model.observation.GetObservationResponse
import com.example.md3.data.model.observation.ObservationResponse
import com.example.md3.data.model.observation.ObservationResult
import com.example.md3.data.model.visit.CreateVisitResponse
import com.example.md3.data.model.visit.CreateVisitSubmitResponse
import com.example.md3.data.model.visit.VisitSubmitedResponse
import com.example.md3.data.source.home.CommissioningRepo
import com.example.md3.utils.network.Resource
import com.example.md3.view.commissioning.CommissioningViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class BreakdownViewModel(private val commissioningRepo: CommissioningRepo) : ViewModel() {


    private val currentQueryForNew = MutableLiveData(DEFAULT_QUERY_MAP_NEW)
    private val currentQueryForOpen = MutableLiveData(DEFAULT_QUERY_MAP_OPEN)
    private val currentQueryForClosed = MutableLiveData(DEFAULT_QUERY_MAP_CLOSED)
    private val currentQueryForPastVisit = MutableLiveData(DEFAULT_QUERY_MAP_PAST_VISIT)
    private val currentQueryForMyVisit = MutableLiveData(DEFAULT_QUERY_MAP_MY_VISIT)
    private val currentQueryForObservation = MutableLiveData(DEFAULT_QUERY_MAP_OBSERVATION)


    companion object {
        val DEFAULT_QUERY_MAP_NEW = hashMapOf<String, Any>("status" to "Pending")
        val DEFAULT_QUERY_MAP_OPEN =
            hashMapOf<String, Any>("status__in" to "Scheduled,Journey Started,Journey Stopped,Work Started,Reached Customer Location")
        val DEFAULT_QUERY_MAP_CLOSED = hashMapOf<String, Any>("" to "")
        val DEFAULT_QUERY_MAP_PAST_VISIT = hashMapOf<String, Any>("" to "")
        val DEFAULT_QUERY_MAP_MY_VISIT = hashMapOf<String, Any>("" to "")
        val DEFAULT_QUERY_MAP_OBSERVATION = hashMapOf<String, Any>("" to "")

    }

    var getAllNewCases = currentQueryForNew.switchMap { currentQuery ->
        commissioningRepo.getBreakdownList(currentQuery).cachedIn(viewModelScope)
    }


    var getAllSuppliers = currentQueryForClosed.switchMap { currentQuery ->
        commissioningRepo.getSuppliers(currentQuery).cachedIn(viewModelScope)
    }


    fun refreshUpcomingVisit(){
        currentQueryForOpen.postValue(DEFAULT_QUERY_MAP_OPEN)
    }


    fun refreshNewCaseList() {
        currentQueryForNew.value = DEFAULT_QUERY_MAP_NEW
    }


    var getAllCases = currentQueryForClosed.switchMap { currentQuery ->
        commissioningRepo.getBreakdownClosedCases(currentQuery).cachedIn(viewModelScope)
    }


//    val getAllPastVisit = currentQueryForPastVisit.switchMap { currentQuery ->
//        commissioningRepo.getAllVisits(currentQuery).cachedIn(viewModelScope)
//    }

    var getAllPastVisit: LiveData<PagingData<VisitResult>>? = null
    var getMyVisit: LiveData<PagingData<VisitResult>>? = null

    fun getMyVisits(organisationUserId: String, commissioningId: String) {
        getMyVisit = commissioningRepo.getMyVisits(
            hashMapOf(
                "assigned_engineer" to organisationUserId,
                "commissioning" to commissioningId
            )
        ).cachedIn(viewModelScope)
    }


    fun getAllVisits(commissioningId: String) {
        getAllPastVisit =
            commissioningRepo.getAllVisits(hashMapOf("commissioning" to commissioningId))
                .cachedIn(viewModelScope)
    }


    fun refreshClosedCaseList() {
        currentQueryForClosed.value = DEFAULT_QUERY_MAP_CLOSED
    }


    var getAllOpenCases = currentQueryForOpen.switchMap { currentQuery ->
        commissioningRepo.getOpenCasesForBreakDown(currentQuery).cachedIn(viewModelScope)
    }


    fun getObservationByVisitID(commissioningVisitId: String): LiveData<PagingData<ObservationResult>> {
        return currentQueryForObservation.switchMap { currentQuery ->
            commissioningRepo.getAllObservations(commissioningVisitId, currentQuery)
                .cachedIn(viewModelScope)
        }
    }


    fun refreshOpenCasesList() {
//        currentQueryForOpen.value = hashMapOf<String, Any>(
//            "status__in" to "open",
//            "status__in" to "In-Progress",
//            "status__in" to "On-Hold"
//        )
    }


    private val _caseRequestMutableLiveData: MutableLiveData<Resource<CaseRequestResponse>> =
        MutableLiveData()
    val caseRequestLiveData = _caseRequestMutableLiveData

    fun changeCaseRequest(status: String, reason: String, commissioningEngineerId: String) {
        _caseRequestMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _caseRequestMutableLiveData.postValue(
                commissioningRepo.changeCaseRequest(
                    status,
                    reason,
                    commissioningEngineerId
                )
            )
        }

    }


    private val _visitDetailsMutableLiveData: MutableLiveData<Resource<GetVisitDetailsResponse>> =
        MutableLiveData()
    val getVisitDetailsMutableLiveData = _visitDetailsMutableLiveData


    fun getVisitDetails(commissioningVisitId: String) {
        _visitDetailsMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _visitDetailsMutableLiveData.postValue(
                commissioningRepo.getVisitDetails(
                    commissioningVisitId
                )
            )
        }
    }


    private val _commissioningDetails: MutableLiveData<Resource<CommissioningDetailsResponse>> =
        MutableLiveData()
    val getCommissioningDetailsMutableLiveData = _commissioningDetails


    fun getCommissioningDetails(
        orgCommissioningID: String
    ) {
        _commissioningDetails.postValue(Resource.loading(null))
        viewModelScope.launch {
            _commissioningDetails.postValue(
                commissioningRepo.getCommissioningDetails(
                    orgCommissioningID
                )
            )
        }
    }


    private val _startJourneyLiveData: MutableLiveData<Resource<StartAndEndJourneyResponse>> =
        MutableLiveData()
    val startJourneyLiveData = _startJourneyLiveData

    fun startJourney(
        commissioningVisitId: String,
        startTime: String
    ) {
        _startJourneyLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _startJourneyLiveData.postValue(
                commissioningRepo.startJourney(commissioningVisitId, startTime)
            )
        }
    }

    private val _endJourneyLiveData: MutableLiveData<Resource<StartAndEndJourneyResponse>> =
        MutableLiveData()
    val endJourneyLiveData = _endJourneyLiveData

    fun endJourney(
        commissioningVisitId: String,
        endTime: String,
        journeyId: String
    ) {
        _endJourneyLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _endJourneyLiveData.postValue(
                commissioningRepo.endJourney(
                    commissioningVisitId,
                    endTime,
                    journeyId
                )
            )
        }
    }


    fun refreshNewCases(){
        currentQueryForNew.postValue(CommissioningViewModel.DEFAULT_QUERY_MAP_NEW)
    }





    private val _journeyVisitEndRouteLiveData: MutableLiveData<Resource<VisitDetailsResponse>> =
        MutableLiveData()
    val journeyVisitEndRouteLiveData = _journeyVisitEndRouteLiveData

    fun journeyVisitEndRoute(
        commissioningVisitId: String,
        journeyVisitRouteResponse: JourneyVisitRouteResponse,
    ) {
        _journeyVisitEndRouteLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _journeyVisitEndRouteLiveData.postValue(
                commissioningRepo.visitRouteJourney(
                    commissioningVisitId,
                    journeyVisitRouteResponse
                )
            )
        }
    }






    private val _journeyVisitRouteLiveData: MutableLiveData<Resource<VisitDetailsResponse>> =
        MutableLiveData()
    val journeyVisitRouteLiveData = _journeyVisitRouteLiveData

    fun journeyVisitRoute(
        commissioningVisitId: String,
        journeyVisitRouteResponse: JourneyVisitRouteResponse,
    ) {
        _journeyVisitRouteLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _journeyVisitRouteLiveData.postValue(
                commissioningRepo.visitRouteJourney(
                    commissioningVisitId,
                    journeyVisitRouteResponse
                )
            )
        }
    }


    private val _observationMutableLiveData: MutableLiveData<Resource<ObservationResponse>> =
        MutableLiveData()
    var observationLiveData = _observationMutableLiveData


    fun createObservation(commissioningVisitId: String, observation: String) {
        _observationMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _observationMutableLiveData.postValue(
                commissioningRepo.createVisitObservation(
                    commissioningVisitId,
                    observation
                )
            )
        }
    }


    private val _editObservationMutableLiveData: MutableLiveData<Resource<ObservationResponse>> = MutableLiveData()
    var editObservationLiveData = _editObservationMutableLiveData

    fun editObservation(commissioningVisitId: String, observation: String , observationId : String) {
        _editObservationMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _editObservationMutableLiveData.postValue(
                commissioningRepo.editVisitObservation(
                    observationId,
                    observation
                )
            )
        }
    }




    private val _getObservationDetailsById: MutableLiveData<Resource<ObservationResult>> = MutableLiveData()
    var getObservationDetailsById = _getObservationDetailsById



    fun getObservationsDetailsById(observationId : String){
        _getObservationDetailsById.postValue(Resource.loading(null))
        viewModelScope.launch {
            _getObservationDetailsById.postValue(
                commissioningRepo.getObservationById(
                    observationId,
                )
            )
        }
    }





    private val _checkSheet: MutableLiveData<Resource<CheckSheetResponse>> =
        MutableLiveData()
    var getAllCheckSheetLiveData = _checkSheet



    private val _submitCheckSheet: MutableLiveData<Resource<CheckSheetResponse>> =
        MutableLiveData()
    var getSubmitCheckSheetLiveData = _submitCheckSheet


    fun getAllCheckSheet(orgCommissioningID: String) {
        getAllCheckSheetLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            getAllCheckSheetLiveData.postValue(
                commissioningRepo.getAllCheckSheet(
                    orgCommissioningID
                )
            )
        }
    }



    fun submitCheckSheet(orgCommissioningID: String,submitCheekSheet: SubmitCheekSheet) {
        _submitCheckSheet.postValue(Resource.loading(null))
        viewModelScope.launch {
            _submitCheckSheet.postValue(
                commissioningRepo.submitCheckSheet(
                    orgCommissioningID = orgCommissioningID,
                    submitCheekSheet = submitCheekSheet
                )
            )
        }
    }


    private val _visitInputMutableLiveData: MutableLiveData<Resource<VisitSubmitedResponse>> =
        MutableLiveData()
    val visitInputLiveData = _visitInputMutableLiveData

    fun visitInput(
        commissioningVisitId: String,
        isAnotherVisitRequired: Boolean,
        endScheduledWorkDate: String,
        endScheduledWorkTime: String,
        remark: String,
        visitStatus: String,
        visitSummary: String,
        explainStatus: String,
        isEngineerKnow: Boolean,
        nextVisitDate: String,
        nextVisitTime: String,
        imageFile: List<MultipartBody.Part>?
    ) {
        _visitInputMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _visitInputMutableLiveData.postValue(
                commissioningRepo.visitInput(
                    commissioningVisitId,
                    isAnotherVisitRequired,
                    endScheduledWorkDate,
                    endScheduledWorkTime,
                    remark,
                    visitStatus,
                    visitSummary,
                    explainStatus,
                    isEngineerKnow,
                    nextVisitDate,
                    nextVisitTime,
                    imageFile
                )
            )
        }
    }


    private val _startWorkLiveData: MutableLiveData<Resource<VisitResult>> = MutableLiveData()
    val startWorkLiveData: LiveData<Resource<VisitResult>> get() = _startWorkLiveData


    private val _endWorkLiveData: MutableLiveData<Resource<VisitResult>> = MutableLiveData()
    val endWorkLiveData: LiveData<Resource<VisitResult>> get() = _endWorkLiveData


    fun startAndEndWork(
        commissioningVisitID: String,
        startTime: String?,
        endTime: String?
    ) {
        if (startTime?.isNotEmpty() == true) {
            _startWorkLiveData.postValue(Resource.loading(null))
        } else {
            _endWorkLiveData.postValue(Resource.loading(null))
        }
        viewModelScope.launch {
            if (startTime?.isNotEmpty() == true) {
                _startWorkLiveData.postValue(
                    commissioningRepo.startAndEndWork(
                        commissioningVisitID,
                        startTime,
                        null
                    )
                )
            } else {
                _endWorkLiveData.postValue(
                    commissioningRepo.startAndEndWork(
                        commissioningVisitID,
                        null,
                        endTime
                    )
                )
            }

        }
    }


    private val _createVisitMutableLiveData: MutableLiveData<Resource<CreateVisitResponse>> =
        MutableLiveData()
    val getCreateVisitLiveData = _createVisitMutableLiveData

    fun createVisit(createVisitSubmitResponse: CreateVisitSubmitResponse) {
        _createVisitMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _createVisitMutableLiveData.postValue(
                commissioningRepo.createVisit(createVisitSubmitResponse)
            )
        }
    }


    private val _concludeMutableLiveData: MutableLiveData<Resource<ConcludeResponse>> =
        MutableLiveData()
    val concludeLiveData = _concludeMutableLiveData

    fun concludeCase(
        org_commissioning_id: String,
        customer_feedback: String,
        name: String,
        email: String,
        phone: String,
        designation : String,
        customer_signature : MultipartBody.Part,
        customer_remark : String
    ) {
        _concludeMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _concludeMutableLiveData.postValue(
                commissioningRepo.concludeCase(
                    org_commissioning_id,
                    customer_feedback,
                    name,
                    email,
                    phone,
                    designation,
                    customer_signature,
                    customer_remark
                )
            )
        }
    }
    private val _getAllObservation: MutableLiveData<Resource<GetObservationResponse>> =
        MutableLiveData()
    val getAllObservation = _getAllObservation


    fun getAllObservations(
        commissioningVisitId: String,
    ) {
        _getAllObservation.postValue(Resource.loading(null))
        viewModelScope.launch {
            _getAllObservation.postValue(commissioningRepo.getAllObservation(commissioningVisitId))
        }
    }


}