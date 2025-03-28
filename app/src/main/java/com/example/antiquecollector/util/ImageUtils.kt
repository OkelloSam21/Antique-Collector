package com.example.antiquecollector.util

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class for handling image operations.
 */
@Singleton
class ImageUtils @Inject constructor(
    private val context: Context
) {
    
    private val contentResolver: ContentResolver = context.contentResolver
    
    /**
     * Saves an image from a Uri to the app's private storage or shared storage
     * depending on Android version.
     *
     * @param sourceUri The source image Uri to save
     * @param useSharedStorage Whether to save to shared storage (for Android 10+)
     * @return The Uri of the saved image
     */
    suspend fun saveImage(sourceUri: Uri, useSharedStorage: Boolean = false): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && useSharedStorage) {
            saveImageToSharedStorage(sourceUri)
        } else {
            saveImageToPrivateStorage(sourceUri)
        }
    }
    
    /**
     * Saves a bitmap to the app's private storage or shared storage
     * depending on Android version.
     *
     * @param bitmap The bitmap to save
     * @param quality The JPEG quality (0-100)
     * @param useSharedStorage Whether to save to shared storage (for Android 10+)
     * @return The Uri of the saved image
     */
    suspend fun saveBitmap(bitmap: Bitmap, quality: Int = 90, useSharedStorage: Boolean = false): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && useSharedStorage) {
            saveBitmapToSharedStorage(bitmap, quality)
        } else {
            saveBitmapToPrivateStorage(bitmap, quality)
        }
    }
    
    /**
     * Gets the file path from a Uri.
     *
     * @param uri The Uri to get the file path from
     * @return The file path, or null if it couldn't be determined
     */
    fun getPathFromUri(uri: Uri): String? {
        val file = File(uri.path ?: return null)
        return if (file.exists()) {
            file.absolutePath
        } else {
            null
        }
    }
    
    /**
     * Loads a bitmap from a Uri.
     *
     * @param uri The Uri to load the bitmap from
     * @return The loaded bitmap, or null if it couldn't be loaded
     */
    fun loadBitmap(uri: Uri): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Delete an image file by its Uri.
     *
     * @param uri The Uri of the image to delete
     * @return True if the image was deleted successfully, false otherwise
     */
    fun deleteImage(uri: Uri): Boolean {
        return try {
            if (uri.scheme == "content") {
                contentResolver.delete(uri, null, null) > 0
            } else {
                val file = File(uri.path ?: return false)
                file.delete()
            }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Saves an image from a Uri to the app's private storage.
     */
    private fun saveImageToPrivateStorage(sourceUri: Uri): Uri {
        val imagesDir = getPrivateImagesDir()
        val newFile = createImageFile(imagesDir)
        
        contentResolver.openInputStream(sourceUri)?.use { input ->
            FileOutputStream(newFile).use { output ->
                input.copyTo(output)
            }
        }
        
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            newFile
        )
    }
    
    /**
     * Saves a bitmap to the app's private storage.
     */
    private fun saveBitmapToPrivateStorage(bitmap: Bitmap, quality: Int): Uri {
        val imagesDir = getPrivateImagesDir()
        val file = createImageFile(imagesDir)
        
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }
        
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
    
    /**
     * Saves an image from a Uri to shared storage (Android 10+).
     */
    private fun saveImageToSharedStorage(sourceUri: Uri): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val filename = "JPEG_${timestamp}.jpg"
        
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AntiqueCollector")
            }
        }
        
        val imageUri = contentResolver.insert(imageCollection, imageDetails) ?: throw Exception("Failed to create new MediaStore record")
        
        contentResolver.openInputStream(sourceUri)?.use { input ->
            contentResolver.openOutputStream(imageUri)?.use { output ->
                input.copyTo(output)
            }
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageDetails.clear()
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(imageUri, imageDetails, null, null)
        }
        
        return imageUri
    }
    
    /**
     * Saves a bitmap to shared storage (Android 10+).
     */
    private fun saveBitmapToSharedStorage(bitmap: Bitmap, quality: Int): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val filename = "JPEG_${timestamp}.jpg"
        
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/AntiqueCollector")
            }
        }
        
        val imageUri = contentResolver.insert(imageCollection, imageDetails) ?: throw Exception("Failed to create new MediaStore record")
        
        contentResolver.openOutputStream(imageUri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageDetails.clear()
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(imageUri, imageDetails, null, null)
        }
        
        return imageUri
    }
    
    /**
     * Gets the private directory for storing images.
     */
    private fun getPrivateImagesDir(): File {
        val imagesDir = File(context.filesDir, "images")
        if (!imagesDir.exists()) {
            imagesDir.mkdirs()
        }
        return imagesDir
    }
    
    /**
     * Creates a new image file with a unique name.
     */
    private fun createImageFile(directory: File): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        return File.createTempFile(imageFileName, ".jpg", directory)
    }
}