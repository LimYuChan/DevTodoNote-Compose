package com.note.core.database.extension

import android.database.SQLException
import android.database.sqlite.SQLiteFullException
import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> safeDbCall(action: suspend () -> T): Result<T, DataError.Local> {
    return try {
        Result.Success(action.invoke())
    } catch (e: Exception) {
        when (e) {
            is SQLiteFullException -> Result.Error(DataError.Local.DISK_FULL)
            is SQLException -> Result.Error(DataError.Local.SQL_EXCEPTION)
            is CancellationException -> throw e
            else -> Result.Error(DataError.Local.UNKNOWN)
        }.also { e.printStackTrace() }
    }
}

fun <T, R> Flow<T>.safeDbCallAsFlow(action: suspend (T) -> R): Flow<Result<R, DataError.Local>> =
    map { data ->
        Result.Success(action.invoke(data)) as Result<R, DataError.Local>
    }.catch { e ->
        emit(
            when (e) {
                is SQLiteFullException -> Result.Error(DataError.Local.DISK_FULL)
                is SQLException -> Result.Error(DataError.Local.SQL_EXCEPTION)
                is CancellationException -> throw e
                else -> Result.Error(DataError.Local.UNKNOWN)
            }.also { e.printStackTrace() }
        )
    }