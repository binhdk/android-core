package com.binh.core.example.ui.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object HistoryRoute

fun NavController.navigateToHistory(navOptions: NavOptions) =
    navigate(route = HistoryRoute, navOptions)

fun NavGraphBuilder.historyScreen() {
    composable<HistoryRoute> {
        HistoryScreen()
    }
}
