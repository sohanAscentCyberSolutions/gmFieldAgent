package com.example.md3.data.source.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.md3.data.model.breakdown.WarrantyConsumedResult
import com.example.md3.data.network.auth.CommissioningApi
import com.example.md3.utils.network.ResponseHandler
import retrofit2.HttpException


class WarrantyConsumedPagingSource(
    private val organizationID : String,
    private val assignedEngineer : String,
    private val api: CommissioningApi,
    private val responseHandler: ResponseHandler,
    private val queryMap: HashMap<String, Any>,
) : PagingSource<Int, WarrantyConsumedResult>() {

    private val TAG = "PostPagingSource"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WarrantyConsumedResult> {
        return try {
            val position = params.key ?: 1
            val response = responseHandler.handleSuccess(api.requestForWarrantyConsumed(organisationID = organizationID, assignedEngineer = assignedEngineer ,  page = position , type = "BreakDown" , queryMap = queryMap))
            val results = generateDummyData()
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

    override fun getRefreshKey(state: PagingState<Int, WarrantyConsumedResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


    fun generateDummyData(): List<WarrantyConsumedResult> {
        return listOf(
            WarrantyConsumedResult(
                id = "1",
                serviceType = "Service Type 1",
                product = "Product 1",
                partClaimed = "Part Claimed 1",
                partNumber = "Part Number 1"
            ),
            WarrantyConsumedResult(
                id = "2",
                serviceType = "Service Type 2",
                product = "Product 2",
                partClaimed = "Part Claimed 2",
                partNumber = "Part Number 2"
            ),
            WarrantyConsumedResult(
                id = "3",
                serviceType = "Service Type 3",
                product = "Product 3",
                partClaimed = "Part Claimed 3",
                partNumber = "Part Number 3"
            )
            // Add more dummy data as needed
        )
    }



}