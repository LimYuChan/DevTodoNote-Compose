package com.note.note.data.repository

import com.note.core.common.NoteDispatcher
import com.note.core.common.annotation.Dispatcher
import com.note.core.data.network.safeCall
import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.note.data.mapper.toModel
import com.note.note.data.service.GithubEventService
import com.note.note.domain.model.RepositoryEvent
import com.note.note.domain.repository.GithubEventRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject


class GithubEventRepositoryImpl @Inject constructor(
    private val service: GithubEventService,
    @Dispatcher(NoteDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) : GithubEventRepository {

    override suspend fun getRepositoryEvents(owner: String, repositoryName: String, branchName: String): Result<List<RepositoryEvent>, DataError.Network> {
        return withContext(ioDispatcher) {
            val result = safeCall { service.getUserRepositoryEvents(owner, repositoryName) }
            when(result) {
                is Result.Success -> Result.Success(result.data.map { it.toModel() })
                is Result.Error -> result
            }
        }
    }
}
