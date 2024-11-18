package com.note.auth.domain.repository

import com.note.core.domain.model.AuthInfo
import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result

interface AuthRepository {
    suspend fun login(code: String): Result<AuthInfo, DataError.Network>
}