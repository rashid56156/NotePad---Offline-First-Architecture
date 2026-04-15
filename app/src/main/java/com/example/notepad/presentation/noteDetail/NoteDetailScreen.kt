package com.example.notepad.presentation.noteDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val contentFocusRequester = remember { FocusRequester() }

    // Collect one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is NoteDetailEvent.NavigateBack -> onNavigateBack()
                is NoteDetailEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    // Auto-focus content field for new notes
    LaunchedEffect(state.isNewNote) {
        if (state.isNewNote) {
            contentFocusRequester.requestFocus()
        }
    }

    // Handle hardware back — save on back if content exists
    BackHandler {
        if (state.title.isNotBlank() || state.content.isNotBlank()) {
            viewModel.onIntent(NoteDetailIntent.SaveNote)
        } else {
            viewModel.onIntent(NoteDetailIntent.NavigateBack)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            NoteDetailTopBar(
                isNewNote = state.isNewNote,
                isSaving = state.isSaving,
                onBack = {
                    if (state.title.isNotBlank() || state.content.isNotBlank()) {
                        viewModel.onIntent(NoteDetailIntent.SaveNote)
                    } else {
                        viewModel.onIntent(NoteDetailIntent.NavigateBack)
                    }
                },
                onSave = { viewModel.onIntent(NoteDetailIntent.SaveNote) },
                onDelete = { viewModel.onIntent(NoteDetailIntent.DeleteNote) }
            )
        }
    ) { padding ->

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Title field — large, clean
            BasicTextField(
                value = state.title,
                onValueChange = { viewModel.onIntent(NoteDetailIntent.TitleChanged(it)) },
                textStyle = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 32.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { inner ->
                    if (state.title.isEmpty()) {
                        Text(
                            text = "Title",
                            style = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.outline,
                                lineHeight = 32.sp
                            )
                        )
                    }
                    inner()
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
            Spacer(modifier = Modifier.height(12.dp))

            // Content field — fills remaining space
            BasicTextField(
                value = state.content,
                onValueChange = { viewModel.onIntent(NoteDetailIntent.ContentChanged(it)) },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 26.sp
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { inner ->
                    if (state.content.isEmpty()) {
                        Text(
                            text = "Start writing...",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.outline,
                                lineHeight = 26.sp
                            )
                        )
                    }
                    inner()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(contentFocusRequester)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteDetailTopBar(
    isNewNote: Boolean,
    isSaving: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = { Text(if (isNewNote) "New Note" else "Edit Note") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            if (!isNewNote) {
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete note",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            IconButton(onClick = onSave, enabled = !isSaving) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Save note")
                }
            }
        }
    )
}
