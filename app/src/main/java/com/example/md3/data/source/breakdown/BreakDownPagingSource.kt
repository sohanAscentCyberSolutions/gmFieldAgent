package com.example.md3.data.source.home

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.md3.data.model.commissioning.new_case.NewCasesResult
import com.example.md3.data.network.auth.CommissioningApi
import com.example.md3.utils.network.ResponseHandler
import retrofit2.HttpException


class BreakDownPagingSource(
    private val organizationID : String,
    private val assignedEngineer : String,
    private val api: CommissioningApi,
    private val responseHandler: ResponseHandler,
    private val queryMap: HashMap<String, Any>,
) : PagingSource<Int, NewCasesResult>() {

    private val TAG = "PostPagingSource"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewCasesResult> {
        return try {
            val position = params.key ?: 1
            val response = responseHandler.handleSuccess(api.requestForNewCases(organisationID = organizationID, assignedEngineer = assignedEngineer ,  page = position , type = "Break Down" , queryMap = queryMap))
            val results = response.data?.results!!
            LoadResult.Page(
                data = results,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.data.next == null) null else position + 1,
            )
        } catch (e: Exception) {
            Log.d(TAG, "load: " + e.localizedMessage)
            LoadResult.Error(e)
        } catch (e : HttpException){
            Log.d(TAG, "load: " + e.localizedMessage)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewCasesResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }


}