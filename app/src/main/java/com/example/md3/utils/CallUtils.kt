package com.codenicely.gimbook.saudi.einvoice.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

class CallUtils {
    companion object {
        fun callAction(context: Context?, number: String) {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:${number.trim()}") //change the number.
            context!!.startActivity(callIntent)
        }


        fun redirectToWhatsApp(context: Context?, number: String) {
            val numberWithCountryCode = "+91$number"
            val url = "https://api.whatsapp.com/send?phone=$numberWithCountryCode"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context!!.startActivity(i)
        }


    }

}

