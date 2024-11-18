package com.note.note.domain.repository

import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.note.domain.model.RepositoryEvent

interface GithubEventRepository {
    suspend fun getRepositoryEvents(owner: String, repositoryName: String, branchName: String): Result<List<RepositoryEvent>, DataError.Network>
}