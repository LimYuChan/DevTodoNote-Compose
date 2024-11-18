package com.note.note.presentation.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.note.note.domain.model.NoteImage
import com.note.note.presentation.R


@Composable
fun ContentImagesSection(
    images: List<NoteImage>,
    canDelete: Boolean,
    onDelete: (image: NoteImage) -> Unit = {},
    onSelect: (uri: String) -> Unit
) {
    if (images.isNotEmpty()) {
        ContentSectionHeader(text = R.string.title_image)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(images) { image ->
                ImageCard(
                    imageData = image,
                    canDelete = canDelete,
                    onImageClick = { onSelect.invoke(it) },
                    onDeleteImage = { onDelete.invoke(it) }
                )
            }
        }
    }
}
