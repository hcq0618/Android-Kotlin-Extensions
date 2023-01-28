package com.hcq.android.kotlin.extensions.graphic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.hcq.android.kotlin.extensions.view.screenDensity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

private const val DEFAULT_MAX_IMAGE_DATA_LENGTH = 500000 // due to intent data size, limit to 500KB

suspend fun Bitmap?.toBytes(
    quality: Int,
    maxSize: Int = DEFAULT_MAX_IMAGE_DATA_LENGTH,
): ByteArray? {
    if (this == null) {
        return null
    }

    return withContext(Dispatchers.IO) {
        ByteArrayOutputStream().use {
            var compressionQuality = quality
            compress(Bitmap.CompressFormat.PNG, compressionQuality, it)

            var bytes = it.toByteArray()
            while (bytes.size > maxSize && compressionQuality >= 30) {
                it.reset()
                compressionQuality =
                    (compressionQuality * 0.85).toInt() /*reduce x 0.85 and try again*/
                compress(Bitmap.CompressFormat.PNG, compressionQuality, it)
                bytes = it.toByteArray()
            }

            bytes
        }
    }
}

suspend fun Bitmap?.toBytes(): ByteArray? {
    if (this == null) {
        return null
    }

    return withContext(Dispatchers.IO) {
        ByteArrayOutputStream().use {
            compress(Bitmap.CompressFormat.PNG, 100, it)
            it.toByteArray()
        }
    }
}

suspend fun ByteArray.toBimap(): Bitmap? {
    return if (isEmpty()) {
        null
    } else {
        withContext(Dispatchers.IO) {
            try {
                BitmapFactory.decodeByteArray(this@toBimap, 0, size)
            } catch (error: OutOfMemoryError) {
                null
            }
        }
    }
}

@Suppress("DEPRECATION")
fun Drawable?.toBitmap(): Bitmap? {
    return this?.toBitmap(
        config = if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    )
}

fun Bitmap?.toDrawable(context: Context): Drawable? {
    return this?.let { BitmapDrawable(context.resources, it) }
}

fun Bitmap.toRoundedCornerDrawable(
    context: Context,
    radius: Float,
    maxHeight: Int = height
): Drawable {
    var bitmap = this
    if (maxHeight < height) {
        val height = (context.screenDensity * maxHeight).toInt()
        val scale = this.height * 1.0f / maxHeight
        val width = (this.width / scale).toInt()
        bitmap = Bitmap.createScaledBitmap(this, width, height, true)
    }

    return RoundedBitmapDrawableFactory.create(context.resources, bitmap).apply {
        cornerRadius = radius
    }
}
