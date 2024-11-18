package com.note.auth.domain.usecase

import com.note.auth.domain.repository.AuthRepository
import com.note.core.domain.AuthStorage
import com.note.core.domain.result.DataError
import com.note.core.domain.result.EmptyResult
import com.note.core.domain.result.Result
import com.note.core.domain.result.asEmptyDataResult
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val authStorage: AuthStorage
) {

    suspend operator fun invoke(code: String): EmptyResult<DataError.Network> {
        return when(val result = authRepository.login(code)) {
            is Result.Success -> {
                val authInfo = result.data
                if(authInfo.isValid()) {
                    authStorage.set(authInfo)
                    Result.Success(Unit)
                }else{
                    Result.Error(DataError.Network.SERIALIZATION)
                }
            }
            is Result.Error -> {
                result.asEmptyDataResult()
            }
        }
    }
}