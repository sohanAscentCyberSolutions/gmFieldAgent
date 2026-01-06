package com.codenicely.bank.loan.instantloan.cashpo.utils

import android.net.Uri
import android.util.Log
import com.codenicely.gimbook.saudi.einvoice.utils.ConstantStrings
import java.util.*
import java.util.regex.Pattern


object StringUtils {
    private const val TAG = "StringUtils"
    fun getQueryMap(query: String): Map<String, String> {
        Log.d(TAG, "getQueryMap() called with: query = [$query]")
        val params = query.split("&".toRegex()).toTypedArray()
        val map: MutableMap<String, String> = HashMap()
        for (param in params) {
            val name = param.split("=".toRegex()).toTypedArray()[0]
            val value = param.split("=".toRegex()).toTypedArray()[1]
            map[name] = value
        }
        return map
    }

    fun parseURL(url: String, params: Map<String?, String?>): String {
        Log.d(TAG, "parseURL() called with: url = [$url], params = [$params]")
        val builder = Uri.parse(url).buildUpon()
        for (key in params.keys) {
            builder.appendQueryParameter(key, params[key])
        }
        return builder.build().toString()
    }

    fun getInitials(name: String): String {

        val initials = name
            .split(' ')
            .mapNotNull { it.firstOrNull()?.toString() }
            .reduce { acc, s -> acc + s }
        return if (initials.length == 1){
            initials
        } else {
            initials.subSequence(0,2).toString()
        }
    }


    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )



}