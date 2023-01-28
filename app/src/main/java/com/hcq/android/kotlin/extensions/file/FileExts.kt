package com.hcq.android.kotlin.extensions.file

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

private const val DEFAULT_MAX_BITMAP_SIZE = 1920 * 1080

suspend fun File.toBitmap(maxSize: Int = DEFAULT_MAX_BITMAP_SIZE): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val filePath = absolutePath

            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeFile(filePath, options)

            var width = options.outWidth
            var height = options.outHeight
            var sampleSize = 1
            while (width * height > maxSize) {
                width /= 2
                height /= 2
                sampleSize *= 2
            }
            options.inJustDecodeBounds = false

            var bitmap: Bitmap?
            var count = 0
            do {
                options.inSampleSize = sampleSize
                bitmap = BitmapFactory.decodeFile(filePath, options)
                sampleSize *= 2
                count++
            } while (bitmap == null && count < 3)

            bitmap
        } catch (e: Exception) {
            null
        }
    }
}

fun File.toFileUri(context: Context, authority: String = context.packageName): Uri? {
    return try {
        FileProvider.getUriForFile(context, authority, this)
    } catch (e: Exception) {
        null
    }
}