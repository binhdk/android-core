package com.binh.core.example.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.binh.core.example.ui.history.historyScreen
import com.binh.core.example.ui.home_detail.HomeDetailRoute
import com.binh.core.example.ui.home_detail.homeDetailScreen
import com.binh.core.example.ui.setting.settingScreen
import com.binh.core.ui.component.CoreNavigationSuiteScaffold
import kotlin.reflect.KClass


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val navigationDestinations = NavigationDestination.entries

    HomeScreenContent(
        navigationDestinations,
        { onNavItemSelected(navController, it) },
        modifier,
        navController
    )
}

fun onNavItemSelected(navController: NavHostController, destination: NavigationDestination) {
    trace("Navigation: ${destination.name}") {
        val navOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        navController.navigate(destination.route, navOptions)
    }
}

@Composable
internal fun HomeScreenContent(
    navigationDestinations: List<NavigationDestination>,
    onNavItemClicked: (NavigationDestination) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    var currentDestination by remember { mutableStateOf(NavigationDestination.HOME_DETAIL.route) }

    // Observe NavController destination changes to update the selected item
    // in NavigationSuiteScaffold
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            backStackEntry.destination.route?.let { route ->
                // Find which of your defined 'items' matches the current route
                val currentScreen = navigationDestinations.find {
                    it.route == route || backStackEntry.destination.isRouteInHierarchy(it.route::class)
                }
                currentScreen?.let {
                    currentDestination = it.route
                }
            }
        }
    }

    CoreNavigationSuiteScaffold(
        navigationSuiteItems = {
            navigationDestinations.forEach { destination ->
                val selected = currentDestination == destination.route
                item(
                    selected = selected,
                    onClick = {
                        if (!navController.currentDestination.isRouteInHierarchy(destination.route::class)) {
                            onNavItemClicked(destination)
                            currentDestination = destination.route
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = destination.unselectedIcon,
                            contentDescription = stringResource(destination.iconTextId),
                        )
                    },
                    selectedIcon = {
                        Icon(
                            imageVector = destination.selectedIcon,
                            contentDescription = stringResource(destination.iconTextId),
                        )
                    },
                    label = { Text(stringResource(destination.titleTextId)) },
                    modifier =
                        Modifier
                            .testTag("CoreNavItem"),
                )
            }
        },
        modifier = modifier,
        windowAdaptiveInfo = windowAdaptiveInfo,
    ) {
        Scaffold { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .consumeWindowInsets(padding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = HomeDetailRoute,
                ) {
                    homeDetailScreen()
                    historyScreen()
                    settingScreen()
                }
            }
        }
    }
}

private fun Modifier.notificationDot(): Modifier =
    composed {
        val tertiaryColor = MaterialTheme.colorScheme.tertiary
        drawWithContent {
            drawContent()
            drawCircle(
                tertiaryColor,
                radius = 5.dp.toPx(),
                // This is based on the dimensions of the NavigationBar's "indicator pill";
                // however, its parameters are private, so we must depend on them implicitly
                // (NavigationBarTokens.ActiveIndicatorWidth = 64.dp)
                center = center + Offset(
                    64.dp.toPx() * .45f,
                    32.dp.toPx() * -.45f - 6.dp.toPx(),
                ),
            )
        }
    }

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false