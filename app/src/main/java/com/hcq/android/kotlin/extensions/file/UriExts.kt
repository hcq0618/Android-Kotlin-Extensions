package com.hcq.android.kotlin.extensions.file

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.FileUtils
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File

fun Uri?.toFilePath(context: Context): String? {
    if (DocumentsContract.isDocumentUri(context, this)) {
        val documentId = DocumentsContract.getDocumentId(this) // e.g. "image:1234"
        val parts = documentId.split(":".toRegex()).toTypedArray()
        val type = parts[0]

        if (isExternalStorageDocument()) {
            if ("primary".equals(type, ignoreCase = true)) {
                @Suppress("DEPRECATION")
                return "${Environment.getExternalStorageDirectory()}/${parts[1]}"
            }
        } else if (isDownloadsDocument()) {
            // Starting with Android O, this "id" is not necessarily a long (row number),
            // but might also be a "raw:/some/file/path" URL
            if (documentId != null && documentId.startsWith("raw:/")) {
                val rawUri = Uri.parse(documentId)
                return rawUri.path
            } else {
                val contentUriPrefixesToTry = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/my_downloads"
                )
                for (contentUriPrefix in contentUriPrefixesToTry) {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse(contentUriPrefix), documentId.toLong()
                    )
                    val path = contentUri.resolveInfo(context)
                    if (!path.isNullOrEmpty()) {
                        return path
                    }
                }
            }
        } else if (isMediaDocument()) {
            val contentUri = when (type) {
                "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                else -> null
            }

            // query content resolver for MediaStore id column
            val selection = "_id=?"
            val args = arrayOf(parts[1])
            return contentUri.resolveInfo(
                context,
                selection = selection,
                args = args
            )
        }
    }

    if (isContentScheme()) {
        return resolveInfo(context)
    } else if (isFileScheme()) {
        return this?.path
    }
    return this?.path
}

private fun Uri?.isExternalStorageDocument(): Boolean {
    return "com.android.externalstorage.documents".equals(this?.authority, ignoreCase = true)
}

private fun Uri?.isDownloadsDocument(): Boolean {
    return "com.android.providers.downloads.documents".equals(
        this?.authority, ignoreCase = true
    )
}

private fun Uri?.isMediaDocument(): Boolean {
    return "com.android.providers.media.documents".equals(this?.authority, ignoreCase = true)
}

private fun Uri?.isContentScheme(): Boolean {
    return ContentResolver.SCHEME_CONTENT.equals(this?.scheme, ignoreCase = true)
}

private fun Uri?.isFileScheme(): Boolean {
    return ContentResolver.SCHEME_FILE.equals(this?.scheme, ignoreCase = true)
}

private fun Uri?.resolveInfo(
    context: Context,
    @Suppress("DEPRECATION")
    projectionStr: String = MediaStore.Images.Media.DATA,
    selection: String? = null,
    args: Array<String>? = null
): String? {
    this ?: return null

    val projection = arrayOf(projectionStr)
    return context.contentResolver.query(this, projection, selection, args, null)?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(projectionStr)
            if (index >= 0) {
                return it.getString(index)
            }
        }
        return null
    }
}

fun Uri?.toFileName(context: Context, projectionStr: String): String? {
    return resolveInfo(context, projectionStr)
}