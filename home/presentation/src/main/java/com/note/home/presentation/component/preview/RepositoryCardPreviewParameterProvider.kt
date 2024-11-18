package com.note.home.presentation.component.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.note.home.domain.model.Repository

class RepositoryCardPreviewParameterProvider: PreviewParameterProvider<Repository> {
    override val values: Sequence<Repository> = sequenceOf(
        Repository(
            id = 0,
            name = "Test Repository Public",
            private = false,
            htmlUrl = "",
            description = "This is a test description for the repository.",
            updatedAt = "2024-11-05T15:45:30Z",
            language = "Kotlin",
            defaultBranch = "master"
        ),
        Repository(
            id = 1,
            name = "Test Repository Private",
            private = true,
            htmlUrl = "",
            description = "This is a test description for the repository.",
            updatedAt = "2024-11-05T15:45:30Z",
            language = "Kotlin",
            defaultBranch = "master"
        ),
        Repository(
            id = 2,
            name = "Test Repository empty Language",
            private = true,
            htmlUrl = "",
            description = "This is a test description for the repository.",
            updatedAt = "2024-11-05T15:45:30Z",
            language = "",
            defaultBranch = "master"
        )
    )
}