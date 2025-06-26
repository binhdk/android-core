@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.library.jacoco)
    alias(libs.plugins.buildlogic.hilt)
}

android {
    namespace = "com.binh.core.ui"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.data)

    api(libs.androidx.core.ktx)
    api(libs.androidx.collection.ktx)
    api(libs.androidx.activity)
    api(libs.google.material)
    api(libs.androidx.fragment)
    api(libs.androidx.swiperefreshlayout)

    // navigation
    api(libs.androidx.navigation.fragment)
    api(libs.androidx.navigation.ui)

    // Lifecycle Components
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.lifecycle.viewmodel)

    testImplementation(projects.core.testing)

}