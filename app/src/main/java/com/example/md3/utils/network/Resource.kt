package com.example.md3.utils.network;

import Status
import android.util.Log
import org.json.JSONObject

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    var hasBeenHandled = false

    fun isResponseHandled() : Boolean {
        if (hasBeenHandled) {
            return hasBeenHandled
        } else {
            hasBeenHandled = true
            return false
        }
    }


    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(msg: String, data: T?): Resource<T> {

            return Resource(
                Status.ERROR,
                data,
                msg
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }

    }

}



