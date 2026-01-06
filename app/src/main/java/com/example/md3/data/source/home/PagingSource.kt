package com.example.md3.data.source.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.md3.data.model.home.RandomResult
import com.example.md3.data.network.home.HomeApi
import retrofit2.HttpException


//class PagingSource(
//    private val postApi: HomeApi,
//    private val queryMap: HashMap<String, Any>,
//) : PagingSource<Int, RandomResult>() {
//
//    private val TAG = "PostPagingSource"
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RandomResult> {
//        return try {
//            val position = params.key ?: 1
//            val response = postApi.requestForGetUserProfile(page = position , queryMap = queryMap)
//            val results = response.body()?.result!!
//            LoadResult.Page(
//                data = results,
//                prevKey = if (position == 1) null else position - 1,
//                nextKey = if (response.body()!!.result.isEmpty()) null else position + 1,
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        } catch (e : HttpException){
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, RandomResult>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }
//
//
//}