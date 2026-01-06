package com.example.md3.data.source.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.md3.data.model.mrn.MrnResult
import com.example.md3.data.network.auth.CommissioningApi
import com.example.md3.utils.network.ResponseHandler
import retrofit2.HttpException


class SupplerPagingSource(
    private val organizationID : String,
    private val assignedEngineer : String,
    private val api: CommissioningApi,
    private val responseHandler: ResponseHandler,
    private val queryMap: HashMap<String, Any>,
) : PagingSource<Int, MrnResult>() {

    private val TAG = "SupplerPagingSource"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MrnResult> {
        return try {
            val position = params.key ?: 1
            val response = responseHandler.handleSuccess(api.requestForGetMrnList(organisationID = organizationID, orgCommissioningID = assignedEngineer ,  page = position , queryMap = queryMap))
            val results = getDummyData()
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

    override fun getRefreshKey(state: PagingState<Int, MrnResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }



    fun getDummyData() : List<MrnResult>{
       return  listOf(
            MrnResult("echp1234", "1234", "1234", "1234", false, 1234),
            MrnResult("echp5678", "5678", "5678", "5678", false, 5678),
            MrnResult("echp91011", "91011", "91011", "91011", true, 91011),
            MrnResult("echp121314", "121314", "121314", "121314", true, 121314),
            MrnResult("echp151617", "151617", "151617", "151617", true, 151617),
            MrnResult("echp181920", "181920", "181920", "181920", true, 181920),
            MrnResult("echp212223", "212223", "212223", "212223", true, 212223),
            MrnResult("echp242526", "242526", "242526", "242526", true, 242526),
            MrnResult("echp272829", "272829", "272829", "272829", true, 272829),
            MrnResult("echp303132", "303132", "303132", "303132", true, 303132),
            MrnResult("echp333435", "333435", "333435", "333435", true, 333435)
        )

    }


}