@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.buildlogic.android.library)
}

android {
    namespace = "com.binh.core.testing"
}

dependencies {

    api(libs.junit)
    api(libs.androidx.test.ext)
    api(libs.androidx.test.core)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.espresso.core)

    api(libs.hilt.android.testing)

    api(libs.kotlinx.coroutines.test)
    api(libs.room.testing)

    //  cryptographic for testing
    implementation(libs.bouncycastle.bcprov)
    implementation(libs.bouncycastle.bcpkix)

    api(projects.core.common)
    api(projects.core.data)
}