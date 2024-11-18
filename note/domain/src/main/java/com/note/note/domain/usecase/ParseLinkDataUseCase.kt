package com.note.note.domain.usecase

import com.note.core.domain.result.DataError
import com.note.core.domain.result.Result
import com.note.note.domain.model.NoteLink
import com.note.note.domain.repository.LinkMetaDataFetcher
import javax.inject.Inject

class ParseLinkDataUseCase @Inject constructor(
    private val fetcher: LinkMetaDataFetcher
) {
    suspend operator fun invoke(link: String): Result<NoteLink, DataError.Parse> {
        val parseLink = if (link.startsWith("http://") || link.startsWith("https://")) {
            link
        } else {
            "https://$link"
        }
        return fetcher.fetch(parseLink)
    }
}