package com.example.md3.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.md3.data.model.checksheet.CheckSheetResponse
import com.example.md3.data.model.checksheet.Section
import com.example.md3.data.model.mrn.MrnDetailsItem
import com.example.md3.data.model.mrn.MrnItem
import com.example.md3.data.model.mrn.MrnResult


class SharedViewModel : ViewModel() {



    val setCheckSetMutableLiveData : MutableLiveData<CheckSheetResponse> = MutableLiveData()
    val setSectorMutableLiveData : MutableLiveData<Section> = MutableLiveData()
    val mrnSelectedQnty : MutableLiveData<MutableList<MrnItem>> = MutableLiveData(mutableListOf())
    val mrnDetailsItemSelected : MutableLiveData<MrnDetailsItem> = MutableLiveData()
    val isRefreshRequired : MutableLiveData<Boolean?> = MutableLiveData(false)
    val isRefreshRequiredCommissioning : MutableLiveData<Boolean?> = MutableLiveData(false)
    val isRefreshRequiredNewCases : MutableLiveData<Boolean?> = MutableLiveData(false)
    var isCheckSheetEdited : MutableLiveData<Boolean> = MutableLiveData()






    fun addItemToList(item: MrnItem) {
        val currentList = mrnSelectedQnty.value ?: mutableListOf()
        val itemExists = currentList.any { it == item }
        if (!itemExists) {
            currentList.add(item)
            mrnSelectedQnty.value = currentList
        }
    }






    fun setCheckSheetResponse(checkSheetResponse : CheckSheetResponse){
        setCheckSetMutableLiveData.postValue(checkSheetResponse)
    }


    fun setSectionResponse(section: Section){
        setSectorMutableLiveData.postValue(section)
    }



     val tabChangeLiveData : MutableLiveData<TabType> = MutableLiveData()
    fun setTabData(tab : TabType){
        tabChangeLiveData.postValue(tab)
    }





    val getWorkTimeLiveData : MutableLiveData<Triple<Int,Int,Int>> = MutableLiveData()

    fun setWorkTime(hours : Int , minutes : Int , seconds : Int){
        getWorkTimeLiveData.postValue(Triple(hours, minutes , seconds))
    }


    val getCurrentLocation : MutableLiveData<Pair<String,String>> = MutableLiveData()
    fun setCurrentLocation(address: Pair<String,String>){
        getCurrentLocation.postValue(address)
    }

     val getCaseID : MutableLiveData<String> = MutableLiveData()



    private val selectedPartsLiveData: MutableLiveData<MutableList<MrnResult>> = MutableLiveData()
    val getSelectedPartsLiveData: LiveData<MutableList<MrnResult>> = selectedPartsLiveData

    fun setSelectedPartList(list: List<MrnResult>) {
        selectedPartsLiveData.value = list.toMutableList()
    }

    fun addSelectedPart(part: MrnResult) {
        val currentList = selectedPartsLiveData.value ?: mutableListOf()
        currentList.add(part)
        selectedPartsLiveData.value = currentList
    }

    fun removeSelectedPart(part: MrnResult) {
        val currentList = selectedPartsLiveData.value ?: return
        currentList.remove(part)
        selectedPartsLiveData.value = currentList
    }



    //

    private val selectedFailureLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()
    val getSelectedFailureLiveData: LiveData<MutableList<String>> = selectedFailureLiveData

    fun setSelectedFailureList(list: List<String>) {

        selectedFailureLiveData.value = list.toMutableList()
    }

    fun addSelectedFailure(part: String) {
        val currentList = selectedFailureLiveData.value ?: mutableListOf()
        currentList.add(part)
        selectedFailureLiveData.value = currentList
    }

    fun removeSelectedFailure(part: String) {
        val currentList = selectedFailureLiveData.value ?: return
        currentList.remove(part)
        selectedFailureLiveData.value = currentList
    }






    fun setCaseID(caseID: String){
        getCaseID.postValue(caseID)
    }
}