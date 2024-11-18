package com.note.core.designsystem.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class SpacerSize{
    Small, 
    Medium, 
    Large
}

@Composable
fun NoteVerticalSpacer(spacerSize: SpacerSize = SpacerSize.Small) {
    val size = when(spacerSize){
        SpacerSize.Small -> 8.dp
        SpacerSize.Medium -> 16.dp
        SpacerSize.Large -> 24.dp
    }
    Spacer(modifier = Modifier.height(size))
}

@Composable
fun NoteHorizontalSpacer(spacerSize: SpacerSize = SpacerSize.Small) {
    val size = when(spacerSize){
        SpacerSize.Small -> 8.dp
        SpacerSize.Medium -> 16.dp
        SpacerSize.Large -> 24.dp
    }
    Spacer(modifier = Modifier.width(size))
}