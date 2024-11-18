package com.note.home.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.note.core.data.network.PagingConstant.GITHUB_STARTING_PAGE_INDEX
import com.note.core.data.network.PagingConstant.PAGING_SIZE
import com.note.home.data.HomeService
import com.note.home.data.dto.toUserRepository
import com.note.home.domain.exception.UnauthorizedException
import com.note.home.domain.model.Repository
import retrofit2.HttpException
import java.io.IOException

internal class RepositoryPagingSource(
    private val service: HomeService
) : PagingSource<Int, Repository>() {

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchorPosition)
        return closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX


        return try {
            val response = service.getRepositories(position)
            val repos = response.map { it.toUserRepository() }

            val nextKey = if (repos.isEmpty()) null else position + (params.loadSize / PAGING_SIZE)

            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            handleLoadError(exception)
        } catch (exception: HttpException) {
            if (exception.code() == 401) {
                handleLoadError(UnauthorizedException())
            } else {
                handleLoadError(exception)
            }
        } catch (throwable: Throwable) {
            handleLoadError(throwable)
        }
    }

    private fun handleLoadError(exception: Throwable): LoadResult.Error<Int, Repository> {
        return LoadResult.Error(exception)
    }
}

