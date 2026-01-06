package com.example.md3.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.location.Location
import android.net.Uri
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.md3.R
import com.example.md3.data.model.Path
import com.example.md3.data.model.checksheet.Field
import com.example.md3.data.preferences.SharedPrefs
import com.google.android.material.textfield.TextInputLayout
import com.google.common.base.Joiner
import com.google.common.base.Splitter
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


object KotlinFunctions {


    fun getCaseStatusColor(status: String): Int {
        return when (status) {
            "Open" -> R.color.colorError
            "Pending" -> R.color.colorOnPending
            "In-Progress" -> R.color.colorIn_progress
            "Closed" -> R.color.colorClose
            "Cancelled" -> R.color.colorSuccess_highContrast
            "On-Hold" -> R.color.colorOnPending
            "Work Started" -> R.color.colorIn_progress
            "Scheduled" -> R.color.colorOnPending
            "Continued" -> R.color.colorIn_progress
            "Completed" -> R.color.colorClose
            else -> R.color.colorSuccess_highContrast
        }
    }


     fun setProgressAndColor(progress: Int): Int {
        val colorRes = when {
            progress < 50 -> R.color.schemes_error
            progress in 51..90 -> R.color.progress_progress
            progress > 90 -> R.color.extended_colorSuccess
            else -> R.color.schemes_error
        }
        return colorRes
    }



    fun RecyclerView.addItemDecoration(
        context: Context,
        drawableId: Int = R.drawable.line_divider
    ) {

        val divider = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        ContextCompat.getDrawable(context, drawableId)
            ?.let { divider.setDrawable(it) }
        if (layoutManager !is LinearLayoutManager)
            return
        addItemDecoration(divider)
    }



    fun RecyclerView.addItemDecorationWithoutListItem(context: Context, drawableId: Int = R.drawable.line_divider) {
        val decoration = DividerItemDecorationWithoutLastItem(context, drawableId)
        addItemDecoration(decoration)
    }


    fun convertWithGuava(map: MutableMap<String?, Any>?): String {
        return Joiner.on(",").withKeyValueSeparator("=").join(map)
    }

    fun convertWithGuava(mapAsString: String?): Map<String, String> {
        return Splitter.on(',').withKeyValueSeparator('=').split(mapAsString)
    }








    fun setupEditTextWithAddCustomFields(
        editText: EditText,
        item: Field,
        textWatcherLayout: TextInputLayout,
        onChange: ((String) -> Unit)? = null
    ) {
        editText.hint = item.helpText
        editText.setText(item.value)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onChange?.let { it(s.toString()) }
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when (item) {
                    is Field -> {
                          item.value = s.toString()
                    }
                }
            }
        })
    }




    fun createImagePartsFromURI(context: Context, uris: List<Uri>?): List<MultipartBody.Part>? {
        return uris?.takeIf { it.isNotEmpty() }?.let {
            val parts = ArrayList<MultipartBody.Part>()
            for (uri in it) {
                val inputStream = context.contentResolver.openInputStream(uri)
                val fileName = getFileNameFromUri(context.contentResolver, uri)
                val requestFile = inputStream?.let { inputStream ->
                    RequestBody.create("image/*".toMediaTypeOrNull(), inputStream.readBytes())
                }
                val imagePart = requestFile?.let { requestFile ->
                    MultipartBody.Part.createFormData("image", fileName, requestFile)
                }
                imagePart?.let { parts.add(it) }
            }
            parts.ifEmpty {
                null
            }
        }
    }


//    fun createImagePartFromBitmap(context: Context, bitmap:Bitmap , casId : String): MultipartBody.Part {
//
//        val filename = "cust_sign_$casId-${TimePickerUtils.getCurrentTime()}.png"
//
//        val f = File(context.cacheDir, filename)
//        f.createNewFile()
//
//        val bos = ByteArrayOutputStream()
//        bitmap.compress(CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
//        val bitmapdata = bos.toByteArray()
//        //write the bytes in file
//        var fos: FileOutputStream? = null
//        try {
//            fos = FileOutputStream(f)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        try {
//            fos!!.write(bitmapdata)
//            fos!!.flush()
//            fos!!.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val requestFile = f?.let { file ->
//           // RequestBody.create("image/*".toMediaTypeOrNull())
//            file.asRequestBody("image/*".toMediaTypeOrNull())
//        }
//        val imagePart = requestFile?.let { requestFile ->
//            MultipartBody.Part.createFormData("image", filename, requestFile)
//        }
//        return imagePart!!
//    }


    fun BitmapToFileConverter(context: Context, bitmap: Bitmap): File? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, 90, bytes)
        var destination: File? = File(
            context.cacheDir,
            System.currentTimeMillis().toString() + ".jpeg"
        )
        val fo: FileOutputStream
        try {
            destination!!.createNewFile()
            fo = FileOutputStream(destination)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (e: FileNotFoundException) {
            destination = null
            e.printStackTrace()
        } catch (e: IOException) {
            destination = null
            e.printStackTrace()
        }
        return destination
    }






    fun createPartFromFile(fieldName: String, file: File?): MultipartBody.Part? {
        return file?.let {
            val requestBody: RequestBody =
                it.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(fieldName, it.name, requestBody)
        }
    }



    private fun getFileNameFromUri(uri1: ContentResolver, uri: Uri): String? {
        val cursor = uri1.query(uri, null, null, null, null)
        cursor?.use {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1 && it.moveToFirst()) {
                val displayName = it.getString(displayNameIndex)
                return displayName ?: uri.lastPathSegment
            }
        }
        return uri.lastPathSegment
    }


    fun saveWorkStatus(sharedPrefs : SharedPrefs,id: String, caseID: String, workStatus: String) {
        val hashMap = hashMapOf<String, String>(
            "id" to id,
            "case_Id" to caseID,
            "work_status" to workStatus
        )
        sharedPrefs.saveWorkStatusHashMap(hashMap)
    }



    fun saveJourneyStatus(sharedPrefs : SharedPrefs,id: String, caseID: String, workStatus: String) {
        val hashMap = hashMapOf<String, String>(
            "id" to id,
            "case_Id" to caseID,
            "work_status" to workStatus
        )
        sharedPrefs.saveJourneyStatusHashMap(hashMap)
    }





    fun removeWorkStatusById(sharedPrefs: SharedPrefs, idToRemove: String) {
        val workStatusMap = sharedPrefs.getWorkStatusHashMap()
        val iterator = workStatusMap.entries.iterator()

        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.key == idToRemove) {
                iterator.remove()
            }
        }

        sharedPrefs.saveWorkStatusHashMap(workStatusMap)
    }




    fun removeAllWorkStatus(sharedPrefs: SharedPrefs) {
        sharedPrefs.clearSavedWorkStatusHashMap()
    }


    fun removeAllJourneyStatus(sharedPrefs: SharedPrefs){
        sharedPrefs.clearSavedJourneyStatusHashMap()
    }



    fun YearMonth.displayText(short: Boolean = false): String {
        return "${this.month.displayText(short = short)} ${this.year}"
    }

    fun Month.displayText(short: Boolean = true): String {
        val style = if (short) TextStyle.SHORT else TextStyle.FULL
        return getDisplayName(style, Locale.ENGLISH)
    }

    fun DayOfWeek.displayText(uppercase: Boolean = false): String {
        return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
            if (uppercase) value.uppercase(Locale.ENGLISH) else value
        }
    }

    fun Context.findActivity(): Activity {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("no activity")
    }

    fun getWeekPageTitle(week: Week): String {
        val firstDate = week.days.first().date
        val lastDate = week.days.last().date
        return when {
            firstDate.yearMonth == lastDate.yearMonth -> {
                firstDate.yearMonth.displayText()
            }
            firstDate.year == lastDate.year -> {
                "${firstDate.month.displayText(short = false)} - ${lastDate.yearMonth.displayText()}"
            }
            else -> {
                "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
            }
        }
    }



    fun calculateTotalDistance(pathList: List<Path>): Float {
        var totalDistance = 0f
        for (i in 1 until pathList.size) {
            val prevPath = pathList[i - 1]
            val prevLat = prevPath.latitude
            val prevLng = prevPath.longitude

            val currentPath = pathList[i]
            val currentLat = currentPath.latitude
            val currentLng = currentPath.longitude

            val location1 = Location("Point A")
            location1.latitude = prevLat
            location1.longitude = prevLng

            val location2 = Location("Point B")
            location2.latitude = currentLat
            location2.longitude = currentLng

            val distanceInMeters = location1.distanceTo(location2) // Distance in meters
            val distanceInKm = distanceInMeters / 1000.0f // Convert meters to kilometers

            totalDistance += distanceInKm
        }
        return totalDistance
    }


}