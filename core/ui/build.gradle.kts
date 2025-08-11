@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.buildlogic.android.library)
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
    api(libs.androidx.activity)
    api(libs.google.material)

    // Lifecycle Components
    api(libs.androidx.lifecycle.runtime)
    api(libs.androidx.lifecycle.viewmodel)

    testImplementation(projects.core.testing)

}