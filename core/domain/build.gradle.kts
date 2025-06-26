@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.library.jacoco)
    alias(libs.plugins.buildlogic.hilt)
}

android {
    namespace = "com.binh.core.domain"
}

dependencies {

    api(projects.core.common)
    api(projects.core.data)

    testImplementation(projects.core.testing)

}