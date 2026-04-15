package com.example.notepad.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notepad.presentation.noteDetail.NoteDetailScreen
import com.example.notepad.presentation.noteList.NoteListScreen

sealed class Screen(val route: String) {
    data object NoteList : Screen("note_list")
    data object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long?) = "note_detail/${noteId ?: -1}"
    }
}

@Composable
fun NoteNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
        composable(Screen.NoteList.route) {
            NoteListScreen(
                onNavigateToDetail = { noteId ->
                    navController.navigate(Screen.NoteDetail.createRoute(noteId))
                }
            )
        }

        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            NoteDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
