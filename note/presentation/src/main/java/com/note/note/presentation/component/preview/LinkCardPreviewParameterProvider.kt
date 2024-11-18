package com.note.note.presentation.component.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.note.note.domain.model.NoteLink
import com.note.note.presentation.write.state.NoteLinkUi

class LinkCardPreviewParameterProvider: PreviewParameterProvider<NoteLinkUi> {
    val templateLink = NoteLink(
        title = "네이버",
        description = "네이버 Test Preview parameter",
        image = "https://www.naver.com/favicon.ico?1",
        link = "https://www.naver.com"
    )
    override val values: Sequence<NoteLinkUi>
        get() = sequenceOf(
            NoteLinkUi(
                link = templateLink
            ),
            NoteLinkUi(
                isLoading = false,
                link = templateLink.copy(title = "")
            ),
            NoteLinkUi(
                isLoading = false,
                link = templateLink.copy(description = "")
            ),
            NoteLinkUi(
                isLoading = false,
                link = templateLink.copy(image = "")
            ),
            NoteLinkUi(
                isLoading = false,
                link = templateLink.copy(link = "")
            )
        )
}