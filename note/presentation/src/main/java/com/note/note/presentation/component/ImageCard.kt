package com.note.note.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.note.core.designsystem.NoteShape
import com.note.core.designsystem.NoteShape50
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.NoteWhite
import com.note.note.domain.model.NoteImage
import com.note.note.presentation.R
@Composable
fun ImageCard(
    imageData: NoteImage,
    canDelete: Boolean ,
    onDeleteImage: (NoteImage) -> Unit = {},
    onImageClick: (uriPath: String) -> Unit
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = NoteShape),
        modifier = Modifier
            .padding(8.dp)
            .size(120.dp),
        onClick = { onImageClick(imageData.uriPath) }
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageData.uriPath)
                    .crossfade(true)
                    .build(),
                contentDescription = imageData.uriPath,
                placeholder = painterResource(id = R.drawable.ic_not_found_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if(canDelete) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(NoteShape50)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = stringResource(id = R.string.image_delete),
                        tint = NoteWhite,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .clickable { onDeleteImage(imageData) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ImageCardPreview() {
    NoteTheme {
        Column {
            ImageCard(
                imageData = NoteImage(
                    uriPath = "https://s.pstatic.net/static/www/mobile/edit/202401"
                ),
                canDelete = false,
                onImageClick = {},
                onDeleteImage = {}
            )
            ImageCard(
                imageData = NoteImage(
                    uriPath = "https://s.pstatic.net/static/www/mobile/edit/202401"
                ),
                canDelete = true,
                onImageClick = {},
                onDeleteImage = {}
            )
        }
    }
}