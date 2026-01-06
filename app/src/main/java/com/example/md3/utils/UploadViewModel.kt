package com.example.md3.utils

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UploadViewModel(application: Application) : AndroidViewModel(application) {

    data class ViewState(
        val imageBitmaps: List<ImageBitmap> = emptyList(),
        val imageUri : List<Uri?> = emptyList()
    )





    val userProfilePicLiveData = MutableLiveData<Uri>()

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

//    fun setImageUrls(urls: List<Uri?>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val imageBitmaps = mutableListOf<ImageBitmap>()
//            val contentResolver = getContext().contentResolver
//
//            urls.forEach { url ->
//                val uri = url
//                if (uri != null) {
//                    contentResolver.openInputStream(uri)?.use { inputStream ->
//                        val imageBitmap = BitmapFactory.decodeStream(inputStream).asImageBitmap()
//                        imageBitmaps.add(imageBitmap)
//                    }
//                }
//            }
//
//            _viewState.update { currentState ->
//                currentState.copy(imageBitmaps = imageBitmaps, imageUri = urls)
//            }
//        }
//    }


    fun setImageUrls(urls: List<Uri?>) {
        viewModelScope.launch(Dispatchers.IO) {
            val contentResolver = getContext().contentResolver

            // Create a copy of the existing ViewState to modify
            _viewState.update { currentState ->
                // Copy the list of existing image bitmaps
                val newImageBitmaps = currentState.imageBitmaps.toMutableList()

                // Load new images and append them to the list
                urls.forEach { url ->
                    val uri = url
                    if (uri != null) {
                        contentResolver.openInputStream(uri)?.use { inputStream ->
                            val imageBitmap = BitmapFactory.decodeStream(inputStream).asImageBitmap()
                            newImageBitmaps.add(imageBitmap)
                        }
                    }
                }

                // Create a new ViewState with the updated image list and return it
                currentState.copy(imageBitmaps = newImageBitmaps, imageUri = urls)
            }
        }
    }





    val selectedImagesListOf : MutableLiveData<MutableList<Uri>> = MutableLiveData()

    fun setSelectedImageList(list: MutableList<Uri>) {
        val currentList = selectedImagesListOf.value ?: mutableListOf()
        val newList = mutableListOf<Uri>()

        list.forEach { uri ->
            if (!currentList.contains(uri)) {
                newList.add(uri)
            }
        }

        currentList.addAll(newList)
        selectedImagesListOf.postValue(currentList)
    }


    fun addSelectedImage(uri: Uri) {
        val currentList = selectedImagesListOf.value ?: mutableListOf()
        currentList.add(uri)
        selectedImagesListOf.postValue(currentList)
    }

//    fun removeSelectedImage(uri: Uri) {
//        val currentList = selectedImagesListOf.value ?: mutableListOf()
//        currentList.remove(uri)
//        selectedImagesListOf.postValue(currentList)
//    }

    fun removeSelectedImage(position: Int) {
        val currentList = selectedImagesListOf.value ?: mutableListOf()
        if (position in 0 until currentList.size) {
            currentList.removeAt(position)
            selectedImagesListOf.postValue(currentList)
        }
    }

















//    fun saveImageUrlsToLocal(context: Context, imageUrls: List<String>) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val savedImageUris = mutableListOf<Uri?>()
//
//                for (imageUrl in imageUrls) {
//                    // Create a file name for the image
//                    val fileName = "image_${System.currentTimeMillis()}.jpg"
//
//                    // Create a file in the external storage directory
//                    val imageFile = File(
//                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName
//                    )
//
//                    // Download the image from the URL and save it to the file
//                    val bitmap = BitmapFactory.decodeStream(java.net.URL(imageUrl).openStream())
//                    val outputStream: OutputStream = FileOutputStream(imageFile)
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
//                    outputStream.flush()
//                    outputStream.close()
//
//                    // Get the URI for the saved image file
//                    val imageUri = Uri.fromFile(imageFile)
//                    savedImageUris.add(imageUri)
//                }
//
//                // Post the URI results using LiveData
//                userProfilePicLiveData.postValue(savedImageUris.toList())
//            } catch (e: IOException) {
//                e.printStackTrace()
//                userProfilePicLiveData.postValue(null)
//            }
//        }
//    }


    fun setUserProfile(uri : Uri){
        userProfilePicLiveData.postValue(uri)
    }



    private fun getContext(): Context = getApplication<Application>().applicationContext


//    fun removeImages(){
//        _viewState.update { it
//            it.copy(imageBitmaps = emptyList(), imageUri = emptyList())
//        }
//    }


    fun removeImages(){
        selectedImagesListOf.postValue(mutableListOf())
    }

}

