package com.note.home.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.note.core.common.NoteDispatcher
import com.note.core.common.annotation.Dispatcher
import com.note.core.data.network.PagingConstant
import com.note.home.data.HomeService
import com.note.home.domain.model.Repository
import com.note.home.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class HomeRepositoryImpl @Inject constructor(
    private val service: HomeService,
    @Dispatcher(NoteDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
): HomeRepository {

    override fun getRepositories(): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConstant.config,
            pagingSourceFactory = { RepositoryPagingSource(service) }
        ).flow
            .flowOn(ioDispatcher)
    }
}