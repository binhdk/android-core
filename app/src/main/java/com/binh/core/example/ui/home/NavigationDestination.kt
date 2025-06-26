package com.binh.core.example.ui.home

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.binh.core.example.R
import com.binh.core.example.ui.history.HistoryRoute
import com.binh.core.example.ui.home_detail.HomeDetailRoute
import com.binh.core.example.ui.setting.SettingRoute
import com.binh.core.ui.icon.CoreIcons

enum class NavigationDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @get:StringRes val iconTextId: Int,
    @get:StringRes val titleTextId: Int,
    val route: Any,
) {
    HOME_DETAIL(
        selectedIcon = CoreIcons.Upcoming,
        unselectedIcon = CoreIcons.UpcomingBorder,
        iconTextId = R.string.home,
        titleTextId = R.string.home,
        route = HomeDetailRoute,
    ),
    HISTORY(
        selectedIcon = CoreIcons.Bookmarks,
        unselectedIcon = CoreIcons.BookmarksBorder,
        iconTextId = R.string.history,
        titleTextId = R.string.history,
        route = HistoryRoute,
    ),
    SETTING(
        selectedIcon = CoreIcons.Grid3x3,
        unselectedIcon = CoreIcons.Grid3x3,
        iconTextId = R.string.setting,
        titleTextId = R.string.setting,
        route = SettingRoute,
    ),
}
