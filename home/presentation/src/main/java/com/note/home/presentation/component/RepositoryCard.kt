package com.note.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.note.core.designsystem.NoteDivider
import com.note.core.designsystem.NotePrivate
import com.note.core.designsystem.NotePublic
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.getColorByLanguage
import com.note.core.domain.extension.convertCurrentDateTime
import com.note.home.domain.model.Repository
import com.note.home.presentation.R
import com.note.home.presentation.component.preview.RepositoryCardPreviewParameterProvider

@Composable
fun RepositoryCard(
    item: Repository,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() },
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
            ) {
                Text(
                    text = item.name,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis
                )

                if (item.description.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (item.language.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    color = getColorByLanguage(item.language),
                                    shape = CircleShape
                                )
                        )
                        Text(
                            text = item.language,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.default_branch),
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Text(
                        text = item.defaultBranch,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = stringResource(
                        id = R.string.last_updated,
                        item.updatedAt.convertCurrentDateTime()
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
            }

            Text(
                text = if (item.private) {
                    stringResource(id = R.string.repo_private)
                } else {
                    stringResource(id = R.string.repo_public)
                },
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .background(
                        color = if(item.private) {
                            NotePrivate
                        }else{
                            NotePublic
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
        HorizontalDivider(
            color = NoteDivider,
            thickness = 0.5.dp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
fun RepositoryCardPreview(
    @PreviewParameter(RepositoryCardPreviewParameterProvider::class) repository: Repository
) {
    NoteTheme {
        RepositoryCard(item = repository) {}
    }
}
