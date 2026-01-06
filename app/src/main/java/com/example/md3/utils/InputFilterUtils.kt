package com.codenicely.gimbook.saudi.einvoice.utils

import android.text.InputFilter


class InputFilterUtils {
    companion object {

        fun inputFilterOnlyForDigits(): InputFilter {
            var filter = InputFilter { source, start, end, dest, dstart, dend ->
                for (i in start until end) {
                    if (!Character.isDigit(source[i])) {
                        return@InputFilter ""
                    }
                }
                null
            }
            return filter
        }



        fun inputFilterOnlyForDigitsAndAlphabet(): InputFilter {
            val reg = "^[a-zA-Z0-9]*\$".toRegex()
            var filter = InputFilter { source, start, end, dest, dstart, dend ->
                for (i in start until end) {
                    if (!Character.toString(source[i]).matches(reg)) {
                        return@InputFilter ""
                    }
                }
                null
            }
            return filter
        }


        fun inputFilterForMaxLength(len : Int) : InputFilter{
            val maxLengthFilter : InputFilter = InputFilter.LengthFilter(len)
            return maxLengthFilter
        }
    }

}

