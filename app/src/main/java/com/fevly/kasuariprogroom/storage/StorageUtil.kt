/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
package com.fevly.kasuariprogroom.storage

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.documentfile.provider.DocumentFile
import java.io.File

class StorageUtil {

    fun getAppRootDir (context : Context) : File? {
        return  context.getExternalFilesDir(null)
    }

    fun fileToUri (file: File) : Uri {
        return DocumentFile.fromFile(file).uri
    }
    fun uriToDocumentFile (uri : Uri, context : Context) : DocumentFile {
        return DocumentFile.fromSingleUri(context, uri)!!
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    return cursor.getString(displayNameIndex)
                }
            }
        }
        return null
    }

}