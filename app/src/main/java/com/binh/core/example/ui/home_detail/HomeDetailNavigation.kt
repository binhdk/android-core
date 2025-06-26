package com.binh.core.example.ui.home_detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object HomeDetailRoute


fun NavController.navigateToHomeDetail(navOptions: NavOptions) =
    navigate(route = HomeDetailRoute, navOptions)

fun NavGraphBuilder.homeDetailScreen() {
    composable<HomeDetailRoute> {
        HomeDetailScreen()
    }
}
