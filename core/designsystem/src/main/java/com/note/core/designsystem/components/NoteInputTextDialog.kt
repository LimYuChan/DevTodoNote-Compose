package com.note.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.R

@Composable
fun NoteInputTextDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    hint: String,
    content: String = "",
    cancelButtonText: String = stringResource(id = R.string.cancel),
    confirmButtonText: String = stringResource(id = R.string.confirm),
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var inputText by remember { mutableStateOf(content) }

    Dialog(onDismissRequest = { onDismiss.invoke() }) {
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = inputText,
                onValueChange = { newText -> inputText = newText },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = MaterialTheme.typography.bodySmall,
                placeholder = {
                    Text(
                        text = hint,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NoteOutlinedActionButton(
                    text = cancelButtonText,
                    modifier = Modifier.weight(1f)
                ) {
                    onDismiss.invoke()
                }

                NoteActionButton(
                    text = confirmButtonText,
                    enabled = inputText.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    onDismiss.invoke()
                    onConfirm.invoke(inputText)
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    NoteTheme {
        NoteInputTextDialog(
            title = "Title",
            onDismiss = {},
            description = "Description",
            hint = stringResource(id = R.string.dummy_short),
            confirmButtonText = stringResource(id = R.string.complete)
        ) {

        }
    }
}