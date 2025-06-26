@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.buildlogic.android.library)
}

android {
    namespace = "id.zelory.compressor"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}