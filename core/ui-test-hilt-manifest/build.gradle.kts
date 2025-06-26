@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.hilt)
}

android {
    namespace = "com.binh.core.uitesthiltmanifest"
}