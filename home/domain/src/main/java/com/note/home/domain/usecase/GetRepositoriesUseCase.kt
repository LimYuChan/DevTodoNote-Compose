package com.note.home.domain.usecase

import androidx.paging.PagingData
import com.note.home.domain.model.Repository
import com.note.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepositoriesUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    operator fun invoke(): Flow<PagingData<Repository>> = homeRepository.getRepositories()
}