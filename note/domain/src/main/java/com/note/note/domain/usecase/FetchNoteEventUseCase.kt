package com.note.note.domain.usecase

import com.note.core.domain.UserRepository
import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.core.domain.result.map
import com.note.note.domain.enums.BranchState
import com.note.note.domain.model.RepositoryEvent
import com.note.note.domain.repository.GithubEventRepository
import javax.inject.Inject

class FetchNoteEventUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val githubEventRepository: GithubEventRepository
) {

    suspend operator fun invoke(repositoryName: String, branchName: String): Result<BranchState, DataError.Network> {
        val profileResult = userRepository.getProfile()

        if(profileResult is Result.Error) {
            return profileResult
        }

        val owner = (profileResult as Result.Success).data.login
        if(owner.isBlank()) {
            return Result.Error(DataError.Network.NOT_FOUND_USER)
        }

        return githubEventRepository.getRepositoryEvents(owner, repositoryName, branchName).map { events ->
            fetchBranchState(events, branchName)
        }
    }


    private fun fetchBranchState(events: List<RepositoryEvent>, branchName: String): BranchState {

        val branchEvents = events.filter {
            it.ref.equals(branchName, ignoreCase = true) || it.pullRequestHeadRef.equals(branchName, ignoreCase = true)
        }

        return when {
            branchEvents.isEmpty() -> BranchState.TODO
            branchEvents.any { it.pullRequestMerged } -> BranchState.MERGE
            else -> BranchState.COMMIT
        }
    }
}