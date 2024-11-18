package com.note.auth.data

import com.note.auth.data.dto.toAuthInfo
import com.note.auth.domain.repository.AuthRepository
import com.note.core.common.NoteDispatcher
import com.note.core.common.annotation.Dispatcher
import com.note.core.data.network.safeCall
import com.note.core.domain.model.AuthInfo
import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.core.domain.result.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val service: AuthService,
    @Dispatcher(NoteDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
): AuthRepository {

    override suspend fun login(code: String): Result<AuthInfo, DataError.Network> {
        return withContext(ioDispatcher) {
            val result = safeCall { service.generateAccessToken(code = code) }
            result.map { it.toAuthInfo() }
        }
    }
}