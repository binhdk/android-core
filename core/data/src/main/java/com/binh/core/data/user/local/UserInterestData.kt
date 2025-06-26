package com.binh.core.data.user.local

/**
 * Class summarizing user interest data
 */
data class UserInterestData(
    val darkThemeConfig: DarkThemeConfig,
    val useDynamicColor: Boolean,
    val shouldHideOnboarding: Boolean,
)
