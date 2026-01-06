package com.example.md3.data.source.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.md3.data.model.mrn.MrnResult
import com.example.md3.data.network.auth.CommissioningApi
import com.example.md3.utils.network.ResponseHandler
import retrofit2.HttpException


class MrnPagingSource(
    private val organizationID : String,
    private val orgServiceRequestId : String,
    private val api: CommissioningApi,
    private val responseHandler: ResponseHandler,
    private val queryMap: HashMap<String, Any>,
) : PagingSource<Int, MrnResult>() {

    private val TAG = "PostPagingSource"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MrnResult> {
        return try {
            val position = params.key ?: 1
            val response = responseHandler.handleSuccess(api.requestForGetMrnList(organisationID = organizationID, orgCommissioningID =  orgServiceRequestId, page = position , queryMap = queryMap))
            val results = dummyData()
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


    fun dummyData() : List<MrnResult> {
        return  listOf(
            MrnResult("Testing", "Testing" , "Testing" , "Tesitng" , true , 1),
            MrnResult("Testing", "Testing" , "Testing" , "Tesitng" , true , 1),
            MrnResult("Testing", "Testing" , "Testing" , "Tesitng" , true , 1),
            MrnResult("Testing", "Testing" , "Testing" , "Tesitng" , true , 1),
            MrnResult("Testing", "Testing" , "Testing" , "Tesitng" , true , 1),
            MrnResult("Testing", "Testing" , "Testing" , "Tesitng" , true , 1),
        )
    }

    override fun getRefreshKey(state: PagingState<Int, MrnResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }



}