package com.example.md3.view.commissioning

import Status
import Status.ERROR
import Status.LOADING
import Status.SUCCESS
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.md3.data.model.ClaimPart
import com.example.md3.data.model.ClaimPartSubmit
import com.example.md3.data.model.JourneyPathResult
import com.example.md3.data.model.amc.ContractDetails
import com.example.md3.data.model.breakdown.PartWiseWarrantyResult
import com.example.md3.data.model.breakdown.WarrantyConsumedResult
import com.example.md3.data.model.checksheet.CheckSheetResponse
import com.example.md3.data.model.checksheet.SubmitCheekSheet
import com.example.md3.data.model.commissioning.CaseRequestResponse
import com.example.md3.data.model.commissioning.CommissionCheckStatusResponse
import com.example.md3.data.model.commissioning.Details.CommissioningDetailsResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.JourneyVisitRouteResponse
import com.example.md3.data.model.commissioning.JourneyVisitRoute.VisitDetailsResponse
import com.example.md3.data.model.commissioning.StartAndEndJourneyResponse
import com.example.md3.data.model.commissioning.conclude.ConcludeResponse
import com.example.md3.data.model.commissioning.visit.GetVisitDetailsResponse
import com.example.md3.data.model.commissioning.visit.VisitResult
import com.example.md3.data.model.customer.CustomerResult
import com.example.md3.data.model.home.HomeScreenKPI
import com.example.md3.data.model.mrn.MrnDetailsItem
import com.example.md3.data.model.mrn.MrnItem
import com.example.md3.data.model.mrn.MrnResult
import com.example.md3.data.model.mrn.MrnSubmitResponse
import com.example.md3.data.model.mrn.PartResultItem
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
import com.example.md3.data.source.home.CommissioningRepo
import com.example.md3.utils.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.Response

class CommissioningViewModel(private val commissioningRepo: CommissioningRepo) : ViewModel() {


    private val currentQueryForNew = MutableLiveData(DEFAULT_QUERY_MAP_NEW)
    private val currentQueryForMRN = MutableLiveData(DEFAULT_QUERY_MAP_NEW)
    private val currentQueryForOpen = MutableLiveData(DEFAULT_QUERY_MAP_OPEN)
    private val currentQueryForClosed = MutableLiveData(DEFAULT_QUERY_MAP_CLOSED)
    private val currentQueryForPastVisit = MutableLiveData(DEFAULT_QUERY_MAP_PAST_VISIT)
    private val currentQueryForMyVisit = MutableLiveData(DEFAULT_QUERY_MAP_MY_VISIT)
    private val currentQueryForObservation = MutableLiveData(DEFAULT_QUERY_MAP_OBSERVATION)
    private val currentQueryForCustomer = MutableLiveData(DEFAULT_QUERY_MAP_CUSTOMER)


    companion object {

        val DEFAULT_QUERY_MAP_NEW = hashMapOf<String, Any>("status" to "Pending")
        val DEFAULT_QUERY_MAP_MRN = hashMapOf<String, Any>("" to "")
        val DEFAULT_QUERY_MAP_OPEN =
            hashMapOf<String, Any>("status__in" to "Scheduled,Journey Started,Journey Stopped,Work Started,Reached Customer Location")
        val DEFAULT_QUERY_MAP_CLOSED = hashMapOf<String, Any>("" to "")
        val DEFAULT_QUERY_MAP_PAST_VISIT = hashMapOf<String, Any>("" to "")
        val DEFAULT_QUERY_MAP_MY_VISIT = hashMapOf<String, Any>("" to "")
        val DEFAULT_QUERY_MAP_OBSERVATION = hashMapOf<String, Any>("ordering" to "created")
        val DEFAULT_QUERY_MAP_CUSTOMER = hashMapOf<String, Any>("search" to "")
    }


    private val _onVisitDeleteLiveData: MutableLiveData<Resource<Response<Void>>> =
        MutableLiveData()
    val getVisitDeleteLiveData: LiveData<Resource<Response<Void>>> get() = _onVisitDeleteLiveData


    var getAllNewCases = currentQueryForNew.switchMap { currentQuery ->
        commissioningRepo.getCommissioningList(currentQuery).cachedIn(viewModelScope)
    }


    fun refreshUpcomingVisit() {
        currentQueryForOpen.postValue(DEFAULT_QUERY_MAP_OPEN)
    }

    fun refreshNewCases() {
        currentQueryForNew.postValue(DEFAULT_QUERY_MAP_NEW)
    }


    var getAllPartWiseWarranty: LiveData<PagingData<PartWiseWarrantyResult>>? = null
    var getAllWarrantyConsumed: LiveData<PagingData<WarrantyConsumedResult>>? = null


    fun getAllPartWiseWarranty(id: String) {
        getAllPartWiseWarranty =
            commissioningRepo.getPartWiseWarrantyList(queryMap = hashMapOf("id" to id))
                .cachedIn(viewModelScope)
    }


    fun getAllWarrantyConsumed(id: String) {
        getAllWarrantyConsumed =
            commissioningRepo.getWarrantyConsumedList(queryMap = hashMapOf("id" to id))
                .cachedIn(viewModelScope)
    }


    fun getBOMlist(productID: String) =
        commissioningRepo.getBOMList(productID = productID, queryMap = hashMapOf("" to ""))
            .cachedIn(viewModelScope)


    fun getContractList(customerId: String) =
        commissioningRepo.getContractList(customerId).cachedIn(viewModelScope)


    fun getLicensesList(customerId: String) =
        commissioningRepo.getLicensesList(customerId).cachedIn(viewModelScope)


    fun getProductList(customerId: String) =
        commissioningRepo.getProductList(customerId).cachedIn(viewModelScope)


    var getAllMRN: LiveData<PagingData<MrnResult>>? = null


    fun getAllMrnProductList(orgServiceRequestId: String) {
        getAllMRN = commissioningRepo.getAllMRN(orgServiceRequestId, hashMapOf("" to ""))
            .cachedIn(viewModelScope)
    }


    val mrnMutableLiveData: MutableLiveData<Resource<List<MrnItem>>> = MutableLiveData()

    fun getAllMrnProductList(orgServiceRequestId: String, query: String) {
        mrnMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            mrnMutableLiveData.postValue(
                commissioningRepo.getAllMrnList(
                    orgServiceRequestId,
                    hashMapOf("search" to query)
                )
            )
        }
    }


    val mrnDetailsMutableLiveData: MutableLiveData<Resource<List<MrnDetailsItem>>> =
        MutableLiveData()

    fun getAllMrnDetails(orgServiceRequestId: String, query: String) {
        mrnDetailsMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            mrnDetailsMutableLiveData.postValue(
                commissioningRepo.getAllMrnDetails(
                    orgServiceRequestId,
                    hashMapOf("search" to query)
                )
            )
        }
    }


    val getPartsMutableLiveData: MutableLiveData<Resource<List<PartResultItem>>> = MutableLiveData()


    fun getAllPartList(orgServiceRequestId: String, query: String) {
        getPartsMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            getPartsMutableLiveData.postValue(
                commissioningRepo.getAllPartDetails(
                    orgServiceRequestId,
                    hashMapOf("search" to query)
                )
            )
        }
    }


    var getAllSuppliers = currentQueryForClosed.switchMap { currentQuery ->
        commissioningRepo.getSuppliers(currentQuery).cachedIn(viewModelScope)
    }


    fun searchSupplier(query: HashMap<String, Any>) {
        currentQueryForClosed.value = query
    }


    fun searchMRN(query: HashMap<String, Any>) {
        currentQueryForMRN.value = query
    }


    fun refreshNewCaseList() {
        currentQueryForNew.value = DEFAULT_QUERY_MAP_NEW
    }


    var getAllCases = currentQueryForClosed.switchMap { currentQuery ->
        commissioningRepo.getClosedCases(currentQuery).cachedIn(viewModelScope)
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
                "service_request" to commissioningId
            )
        ).cachedIn(viewModelScope)
    }


    fun getAllVisits(commissioningId: String) {
        getAllPastVisit =
            commissioningRepo.getAllVisits(hashMapOf("service_request" to commissioningId))
                .cachedIn(viewModelScope)
    }


    fun refreshClosedCaseList() {
        currentQueryForClosed.value = DEFAULT_QUERY_MAP_CLOSED
    }


    var getAllOpenCases = currentQueryForOpen.switchMap { currentQuery ->
        commissioningRepo.getOpenCases(currentQuery).cachedIn(viewModelScope)
    }


    fun getObservationByVisitID(commissioningVisitId: String): LiveData<PagingData<ObservationResult>> {
        return currentQueryForObservation.switchMap { currentQuery ->
            commissioningRepo.getAllObservations(commissioningVisitId, currentQuery)
                .cachedIn(viewModelScope)
        }
    }


    fun getCustomerList(): LiveData<PagingData<CustomerResult>> {
        return currentQueryForCustomer.switchMap { currentQuery ->
            commissioningRepo.getAllCustomers(currentQuery)
                .cachedIn(viewModelScope)
        }
    }


    fun getCustomerByName(customer_name: String) {
        currentQueryForCustomer.postValue(hashMapOf("search" to customer_name))
    }


    val customerDetailsMutableLiveData: MutableLiveData<Resource<CustomerResult>> =
        MutableLiveData()

    fun getCustomerDetails(customerId: String) {
        customerDetailsMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            customerDetailsMutableLiveData.postValue(commissioningRepo.getCustomerDetails(customerId))
        }
    }


    fun refreshOpenCasesList() {
        currentQueryForOpen.value = hashMapOf<String, Any>(
//            "status__in" to "open",
//            "status__in" to "In-Progress",
//            "status__in" to "On-Hold"
        )
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


    private val _commissioningStatus: MutableLiveData<Resource<CommissionCheckStatusResponse>> =
        MutableLiveData()
    val getCommissioningStatus = _commissioningStatus


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


    fun getCommissioningStatus(
        orgCommissioningID: String
    ) {
        _commissioningStatus.postValue(Resource.loading(null))
        viewModelScope.launch {
            _commissioningStatus.postValue(
                commissioningRepo.getCheckCommissionStatus(
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


    private val _editObservationMutableLiveData: MutableLiveData<Resource<ObservationResponse>> =
        MutableLiveData()
    var editObservationLiveData = _editObservationMutableLiveData

    fun editObservation(commissioningVisitId: String, observation: String, observationId: String) {
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


    private val _getObservationDetailsById: MutableLiveData<Resource<ObservationResult>> =
        MutableLiveData()
    var getObservationDetailsById = _getObservationDetailsById


    fun getObservationsDetailsById(observationId: String) {
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


    fun submitCheckSheet(orgCommissioningID: String, submitCheekSheet: SubmitCheekSheet) {
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


    fun submitVisitInputForBreakdown(
        commissioningVisitId: String,
        isAnotherVisitRequired: Boolean,
        endScheduledWorkDate: String,
        endScheduledWorkTime: String,
        nextVisitDate: String,
        nextVisitTime: String,
        isEngineerKnow: Boolean,
        remark: String,
        isPhysicalCheckPerformed: Boolean,
        isServiceRequired: Boolean,
        requiredParts: MutableList<String>,
        problemDescription: String,
        engineerReview: String,
        failureCategory: String,
        fault: String,
        failureReasonList: MutableList<String>,
        solutionApplied: String,
        solutionDescription: String,
        imageFile: List<MultipartBody.Part>?
    ) {
        _visitInputMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _visitInputMutableLiveData.postValue(
                commissioningRepo.visitInputForBreakdown(
                    commissioningVisitId,
                    isAnotherVisitRequired,
                    endScheduledWorkDate,
                    endScheduledWorkTime,
                    nextVisitDate,
                    nextVisitTime,
                    isEngineerKnow,
                    remark,
                    isPhysicalCheckPerformed,
                    isServiceRequired,
                    failureCategory,
                    fault,
                    solutionApplied,
                    solutionDescription,
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


    private val _statusChangeCaseRequest: MutableLiveData<Resource<VisitResult>> =
        MutableLiveData()
    val getStatusChangeRequest = _statusChangeCaseRequest

    fun changeVisitStatus(
        commissioningVisitId: String, status: String, latitude: String,
        longitude: String
    ) {
        _statusChangeCaseRequest.postValue(Resource.loading(null))
        viewModelScope.launch {
            _statusChangeCaseRequest.postValue(
                commissioningRepo.changeVisitStatus(
                    commissioningVisitId,
                    status,
                    latitude,
                    longitude
                )
            )
        }
    }


    private val _submitRootAnalysis: MutableLiveData<Resource<RootAnalysisResponse>> =
        MutableLiveData()
    val getSubmitRootAnalysisLiveData = _submitRootAnalysis


    fun submitRootAnalysis(commissioningVisitId: String, why1: String, why2: String, why3: String) {
        _submitRootAnalysis.postValue(Resource.loading(null))
        viewModelScope.launch {
            _submitRootAnalysis.postValue(
                commissioningRepo.submitRootAnalysis(commissioningVisitId, why1, why2, why3)
            )
        }
    }


    fun deleteVisit(visitId: String) {
        _onVisitDeleteLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _onVisitDeleteLiveData.postValue(commissioningRepo.requestForDeleteVisit(visitId))
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
        designation: String,
        customer_signature: MultipartBody.Part?,
        customer_remark: String
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


    private val _createMrnResponseLiveData: MutableLiveData<Resource<SubmitMrnResponseItem>> =
        MutableLiveData()
    val getCreateMrnResponseLiveData = _createMrnResponseLiveData

    fun submitMrn(orgServiceRequestId: String, mrnSubmitResponse: MrnSubmitResponse) {
        _createMrnResponseLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _createMrnResponseLiveData.postValue(
                commissioningRepo.submitMrn(
                    orgServiceRequestId,
                    mrnSubmitResponse
                )
            )
        }
    }


    private val _claimPartResponseLiveData: MutableLiveData<Resource<ClaimPartSubmit>> =
        MutableLiveData()
    val getClaimPartResponseLiveData = _claimPartResponseLiveData


    fun submitClaimPart(orgServiceRequestId: String, claimPart: ClaimPart) {
        _claimPartResponseLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _claimPartResponseLiveData.postValue(
                commissioningRepo.submitClaimPart(
                    orgServiceRequestId,
                    claimPart
                )
            )
        }
    }


    private val _amcDetailsResponseLiveData: MutableLiveData<Resource<ContractDetails>> =
        MutableLiveData()
    val getAmcDetailsLiveData = _amcDetailsResponseLiveData


    fun getAmcDetails(orgServiceRequestId: String) {
        _amcDetailsResponseLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _amcDetailsResponseLiveData.postValue(
                commissioningRepo.getAmcDetails(
                    orgServiceRequestId
                )
            )
        }
    }


    private val _warrantyDetailsResponseLiveData: MutableLiveData<Resource<WarrantyInfo>> =
        MutableLiveData()
    val getWarrantyDetailsLiveData = _warrantyDetailsResponseLiveData


    private val _productDetailsResponseLiveData: MutableLiveData<Resource<ProductDetails>> =
        MutableLiveData()
    val getProductDetailsLiveData = _productDetailsResponseLiveData


    private val _homeKPIResponseLiveData: MutableLiveData<Resource<HomeScreenKPI>> =
        MutableLiveData()
    val getHomeKPILiveData = _homeKPIResponseLiveData


    fun getWarrantyDetails(orgServiceRequestId: String) {
        _warrantyDetailsResponseLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _warrantyDetailsResponseLiveData.postValue(
                commissioningRepo.getWarrantyDetails(
                    orgServiceRequestId
                )
            )
        }
    }


    fun getProductDetails(orgProductId: String) {
        _productDetailsResponseLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _productDetailsResponseLiveData.postValue(
                commissioningRepo.getProductDetails(
                    orgProductId
                )
            )
        }
    }


    fun getHomeKPI(search: String) {
        _homeKPIResponseLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _homeKPIResponseLiveData.postValue(commissioningRepo.getHomeKPI(search))
        }
    }


    private val _conclusionDetailsMutableLiveData: MutableLiveData<Resource<ConcludeResponse>> =
        MutableLiveData()
    val getConclusionDetailsMutableLiveData = _conclusionDetailsMutableLiveData


    fun getConclusionDetails(orgCommissioningId: String) {
        _conclusionDetailsMutableLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            _conclusionDetailsMutableLiveData.postValue(
                commissioningRepo.getConclusionDetails(
                    orgCommissioningId
                )
            )
        }
    }







    private val _journeyWithPathMutableLiveData: MutableLiveData<Resource<MutableList<JourneyPathResult>>> =
        MutableLiveData()
    val getJourneyWithPathMutableLiveData = _journeyWithPathMutableLiveData

    fun getJourneyAndResponseWithPath(commissioningVisitId: String) {
        viewModelScope.launch {
            _journeyWithPathMutableLiveData.postValue(Resource.loading(null))
            withContext(Dispatchers.Main) {
                var list: MutableList<JourneyPathResult> = mutableListOf()
                val response = commissioningRepo.getJourneyList(commissioningVisitId)
                when (response.status) {
                    SUCCESS -> {
                        response.data?.journeys?.forEach {
                            val pathResponse = commissioningRepo.getJourneyPath(it.id)
                            if (pathResponse.status == Status.SUCCESS) {
                                list.add(JourneyPathResult(it.id, pathResponse.data?.path!!))
                            }
                        }
                        _journeyWithPathMutableLiveData.postValue(Resource.success(list))
                    }
                    ERROR -> {
                        _journeyWithPathMutableLiveData.postValue(
                            Resource.error(
                                response.message!!,
                                list
                            )
                        )
                    }

                    LOADING -> {
                        _journeyWithPathMutableLiveData.postValue(
                            Resource.loading(list)
                        )
                    }
                }
            }
        }
    }


}