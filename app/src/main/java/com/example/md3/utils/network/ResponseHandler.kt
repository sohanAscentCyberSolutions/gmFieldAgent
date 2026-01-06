package com.example.md3.utils.network;


import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException


open class ResponseHandler {

    private val TAG = "ResponseHandler"

    fun <T : Any> handleSuccess(data: T?): Resource<T> {
        return Resource.success(data)
    }

    fun <T : Any> handleException(e: Exception): Resource<T> {
        Log.e("TAG", "handleException: " + e.stackTrace.toString())
        e.printStackTrace()
//        Log.d("TAG", "handleException: " + e.printStackTrace())
        Log.e("TAG", "handleException: " + e.cause.toString())
        Log.e("TAG", "handleException: " + e.message.toString())



        return when (e) {

            is HttpException -> {

//                var errorMessage =
//                if(e.code()!=400 && e.code()!=403){
//
//                    getErrorMessage(e.code(),e)
//
//                } else{


                val body = e.response()?.errorBody()
                var errorMessage = getErrorJSON(body)


//                Resource.error(getErrorMessage(e.code(),e), null)
                Resource.error(errorMessage, null)
            }

            else -> Resource.error(getErrorMessage(Int.MAX_VALUE, e), null)
        }
    }

//    private fun getErrorJSON(body: ResponseBody?): String {
//        if (body != null) {
//            return try{
//                val mainObject = JSONObject(body.string())
//               if (mainObject.has("detail"))
//                   mainObject.getString("detail").toString()
//               else mainObject.getString(("message")).toString()
//
//            } catch (e:Exception){
//                Log.d("checkErrorMessage4",e.message+"");
//                "Something wrong happened"
//            }
//        }else{
//        return  "Something wrong happened !!!"
//        }
//
//    }


//    private fun getErrorClass(body: ResponseBody?): Any? {
//        body?.let { responseBody ->
//            try {
//                val errorJson = responseBody.string()
//                val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
//                val errorMap = gson.fromJson(errorJson, Map::class.java) as? Map<String, Any>
//                errorMap?.let {
//                    if (it.containsKey("error_class")) {
//                        return it["error_class"]
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d(TAG, "Error parsing error class: ${e.message ?: ""}")
//            }
//        }
//        return null
//    }


    private fun getErrorJSON(body: ResponseBody?): String {
        body?.let { responseBody ->
            try {
                val errorJson = responseBody.string()
                val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
                val errorMap = gson.fromJson(errorJson, Map::class.java) as? Map<String, Any>
                if (errorMap != null && errorMap.containsKey("visit_details")) {
                    val visitDetails = errorMap["visit_details"] as? Map<String, Any>
                    val id = visitDetails?.get("id")
                    return id.toString()
                }else if (errorMap != null && (errorMap.containsKey("detail") || errorMap.containsKey("message"))) {
                    val errorMessage = if (errorMap.containsKey("detail")) {
                        errorMap["detail"].toString()
                    } else {
                        errorMap["message"].toString()
                    }
                    Log.d(TAG, "getErrorJSON: $errorMessage")
                    return errorMessage
                } else {
                    val errorObject = JSONObject(responseBody.string())
                    val errorMessage = StringBuilder()
                    Log.d(TAG, "getErrorJSON: " + errorObject)

                    errorObject.keys().forEach { key ->
                        val errorArray = errorObject.getJSONArray(key)
                        for (i in 0 until errorArray.length()) {
                            errorMessage.append("${errorArray.getString(i)}\n")
                        }
                    }
                    return errorMessage.toString().trim()
                }
            } catch (e: Exception) {
                Log.d("ParseError", e.message ?: "")
            }
        }
        return "Something went wrong"
    }


//    private fun getErrorJSON(body: ResponseBody?): String {
//        body?.let { responseBody ->
//            try {
//                val errorObject = JSONObject(responseBody.string())
//                val errorMessage = StringBuilder()
//                Log.d(TAG, "getErrorJSON: " + errorObject)
//
//                errorObject.keys().forEach { key ->
//                    val errorArray = errorObject.getJSONArray(key)
//                    for (i in 0 until errorArray.length()) {
//                        errorMessage.append("${errorArray.getString(i)}\n")
//                    }
//                }
//                return errorMessage.toString().trim()
//            } catch (e: Exception) {
//                Log.d("ParseError", e.message ?: "")
//            }
//        }
//        return "Something went wrong"
//    }


    private fun getErrorMessage(code: Int, e: Exception): String {
        return when (code) {
            // SocketTimeOut.code -> "Timeout"
            401 -> "Unauthorised"
            404 -> "Not found"
            403 -> "No Access"
            400 -> e.message.toString()
            429 -> "Try after sometime"
            500 -> "Internal Server Error"
            else -> "Something went wrong"
        }
    }
}