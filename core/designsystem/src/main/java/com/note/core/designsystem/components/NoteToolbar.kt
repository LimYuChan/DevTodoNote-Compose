@file:OptIn(ExperimentalMaterial3Api::class)

package com.note.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.note.core.designsystem.ArrowLeftIcon
import com.note.core.designsystem.NotePrimary
import com.note.core.designsystem.NoteTheme
import com.note.core.designsystem.R
import com.note.core.designsystem.components.item.MenuItem

@Composable
fun NoteToolbar(
    title: String = "",
    showBackButton: Boolean = false,
    modifier: Modifier = Modifier,
    menuItemEnabled: Boolean = false,
    menuItems: List<MenuItem> = emptyList(),
    onMenuItemClick: (Int) -> Unit = {},
    onBackClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null
) {
    var isDropDownOpen by rememberSaveable {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                startContent?.invoke()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        },
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = ArrowLeftIcon,
                        contentDescription = stringResource(id = R.string.go_back),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        actions = {
            if (menuItems.isNotEmpty()) {
                if(menuItems.size > 1) {
                    Box {
                        DropdownMenu(
                            expanded = isDropDownOpen,
                            onDismissRequest = {
                                isDropDownOpen = false
                            }
                        ) {
                            menuItems.forEachIndexed { index, item ->
                                Row (
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable {
                                            isDropDownOpen = false
                                            onMenuItemClick(index)
                                        }
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ){
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                        IconButton(onClick = {
                            isDropDownOpen = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = stringResource(id = R.string.open_menu),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }else{
                    IconButton(onClick = { onMenuItemClick.invoke(0) }) {
                        Icon(
                            imageVector = menuItems.first().icon,
                            contentDescription = menuItems.first().title,
                            tint = if(menuItemEnabled) {
                                NotePrimary
                            }else{
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
            }
        }
    )
}


@Preview
@Composable
private fun NoteToolbarPreview() {
    NoteTheme {
        NoteToolbar(
            showBackButton = true,
            title = "Dev Todo Note",
            modifier = Modifier.fillMaxWidth(),
            menuItemEnabled = true,
            menuItems = listOf(MenuItem("test", Icons.Rounded.Done))
        )
    }
}

