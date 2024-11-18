package com.note.note.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.note.core.designsystem.NoteLink
import com.note.core.designsystem.NoteShape
import com.note.core.designsystem.NoteWhite
import com.note.note.presentation.R
import com.note.note.presentation.component.preview.LinkCardPreviewParameterProvider
import com.note.note.presentation.write.state.NoteLinkUi

@Composable
fun LinkCard(
    linkData: NoteLinkUi,
    canDelete: Boolean = false,
    onLinkDelete: (NoteLinkUi) -> Unit = {},
    onLinkSelected: (NoteLinkUi) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = NoteShape),
        modifier = Modifier
            .padding(8.dp)
            .width(300.dp),
        onClick = { onLinkSelected(linkData) }
    ) {
        if (linkData.isLoading) {
            LoadingIndicator()
        } else {
            LinkContent(
                linkData = linkData,
                canDelete = canDelete,
                onDelete = onLinkDelete
            )
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(30.dp))
    }
}

@Composable
private fun LinkContent(
    linkData: NoteLinkUi,
    canDelete: Boolean,
    onDelete: (NoteLinkUi) -> Unit
) {
    Row(
        modifier = Modifier.padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (linkData.link.image.isNotBlank()) {
            LinkImage(imageUrl = linkData.link.image)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            LinkTextContent(linkData = linkData)
        }

        if (canDelete) {
            DeleteButton(onClick = { onDelete(linkData) })
        }
    }
}

@Composable
private fun RowScope.LinkImage(imageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(id = R.string.link_image),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(70.dp)
            .clip(RoundedCornerShape(12.dp))
            .align(Alignment.CenterVertically)
    )
}

@Composable
private fun LinkTextContent(linkData: NoteLinkUi) {
    if (linkData.link.title.isNotBlank()) {
        Text(
            text = linkData.link.title,
            color = NoteWhite,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium
        )
    }
    if (linkData.link.link.isNotBlank()) {
        Text(
            text = linkData.link.link,
            color = NoteLink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall
        )
    }
    if (linkData.link.description.isNotBlank()) {
        Text(
            text = linkData.link.description,
            color = NoteWhite,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            minLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DeleteButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(24.dp)
            .padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(id = R.string.link_delete),
            tint = NoteWhite
        )
    }
}
@Preview
@Composable
fun LinkCardPreview(
    @PreviewParameter(LinkCardPreviewParameterProvider::class) noteLinkUi: NoteLinkUi
) {
    LinkCard(
        linkData = noteLinkUi,
        canDelete = true,
        onLinkSelected = {},
        onLinkDelete = {}
    )
}