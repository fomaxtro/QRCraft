package com.fomaxtro.core.data.util

import android.database.sqlite.SQLiteDiskIOException
import com.fomaxtro.core.domain.error.DataError
import com.fomaxtro.core.domain.util.Result
import kotlinx.coroutines.CancellationException
import timber.log.Timber

inline fun <reified T> safeDatabaseCall(
    block: () -> T
): Result<T, DataError> {
    return try {
        Result.Success(block())
    } catch (e: SQLiteDiskIOException) {
        Timber.e(e)

        Result.Error(DataError.DISK_FULL)
    } catch (e: Exception) {
        if (e is CancellationException) throw e

        Timber.e(e)

        Result.Error(DataError.UNKNOWN)
    }
}