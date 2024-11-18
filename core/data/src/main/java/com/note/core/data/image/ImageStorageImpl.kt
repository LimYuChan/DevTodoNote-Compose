package com.note.core.data.image

import android.content.Context
import androidx.core.net.toUri
import com.note.core.common.NoteDispatcher
import com.note.core.common.annotation.Dispatcher
import com.note.core.domain.ImageStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

internal class ImageStorageImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @Dispatcher(NoteDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
): ImageStorage {
    override suspend fun copyImageToInternalStorage(pathUri: String): String {

        return withContext(ioDispatcher){
            val now = System.currentTimeMillis()
            val inputStream: InputStream? = context.contentResolver.openInputStream(pathUri.toUri())
            val imageFile = File(context.filesDir, "$now.jpeg")

            inputStream?.use { input ->
                FileOutputStream(imageFile).use { output ->
                    input.copyTo(output)
                }
            }
            imageFile.absolutePath
        }
    }
}