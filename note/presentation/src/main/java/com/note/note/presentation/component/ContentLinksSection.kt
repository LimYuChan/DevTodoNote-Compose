package com.note.note.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.note.note.presentation.R
import com.note.note.presentation.write.state.NoteLinkUi


@Composable
fun ContentLinksSection(
    links: List<NoteLinkUi>,
    canDelete: Boolean,
    onDelete: (NoteLinkUi) -> Unit = { },
    onSelect: (url: String) -> Unit
) {
    if (links.isNotEmpty()) {
        ContentSectionHeader(text = R.string.title_link)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(links) { linkUi ->
                LinkCard(
                    linkData = linkUi,
                    canDelete = canDelete,
                    onLinkSelected = { onSelect.invoke(linkUi.link.link) },
                    onLinkDelete = { onDelete.invoke(linkUi) }
                )
            }
        }
    }
}