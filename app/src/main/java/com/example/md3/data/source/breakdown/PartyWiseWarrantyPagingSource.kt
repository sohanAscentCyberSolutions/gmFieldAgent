package com.example.md3.data.source.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.md3.data.model.breakdown.PartWiseWarrantyResult
import com.example.md3.data.network.auth.CommissioningApi
import com.example.md3.utils.network.ResponseHandler
import retrofit2.HttpException


class PartyWiseWarrantyPagingSource(
    private val organizationID : String,
    private val assignedEngineer : String,
    private val api: CommissioningApi,
    private val responseHandler: ResponseHandler,
    private val queryMap: HashMap<String, Any>,
) : PagingSource<Int, PartWiseWarrantyResult>() {

    private val TAG = "PostPagingSource"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PartWiseWarrantyResult> {
        return try {
            val position = params.key ?: 1
            val response = responseHandler.handleSuccess(api.requestForPartyWiseWarranty(organisationID = organizationID, assignedEngineer = assignedEngineer ,  page = position , type = "BreakDown" , queryMap = queryMap))
            val results = generateDummyDataForParts()
            LoadResult.Page(
                data = results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.data?.next == null) null else position + 1,
            )
        } catch (e: Exception) {
            Log.d(TAG, "load: " + e.localizedMessage)
            LoadResult.Error(e)
        } catch (e : HttpException){
            Log.d(TAG, "load: " + e.localizedMessage)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PartWiseWarrantyResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


    fun generateDummyDataForParts(): List<PartWiseWarrantyResult> {
        return listOf(
            PartWiseWarrantyResult(
                id = "1",
                partName = "Part Name 1",
                partNumber = "Part Number 1",
                isUnderWarranty = true,
                warrantyStatusStartDate = "2023-01-01",
                warrantyStatusEndDate = "2025-01-01"
            ),
            PartWiseWarrantyResult(
                id = "2",
                partName = "Part Name 2",
                partNumber = "Part Number 2",
                isUnderWarranty = false,
                warrantyStatusStartDate = "2022-06-01",
                warrantyStatusEndDate = "2024-06-01"
            ),
            PartWiseWarrantyResult(
                id = "3",
                partName = "Part Name 3",
                partNumber = "Part Number 3",
                isUnderWarranty = true,
                warrantyStatusStartDate = "2023-03-15",
                warrantyStatusEndDate = "2025-03-15"
            )
            // Add more dummy data as needed
        )
    }


}