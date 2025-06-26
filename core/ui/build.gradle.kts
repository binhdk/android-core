@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.library.compose)
    alias(libs.plugins.buildlogic.android.library.jacoco)
    alias(libs.plugins.buildlogic.hilt)
}

android {
    namespace = "com.binh.core.ui"
}

dependencies {
    api(projects.core.common)
    api(projects.core.data)

    api(libs.androidx.core.ktx)
    api(libs.androidx.collection.ktx)
    api(libs.androidx.activity.compose)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)

    // Lifecycle Components
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.lifecycle.viewmodel)

    api(libs.androidx.metrics)

    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.compose.ui.testManifest)

    testImplementation(projects.core.testing)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)

}