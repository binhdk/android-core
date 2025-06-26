import buildlogic.CustomProductFlavor

plugins {
    alias(libs.plugins.buildlogic.android.application)
    alias(libs.plugins.buildlogic.android.application.compose)
    alias(libs.plugins.buildlogic.android.application.flavors)
    alias(libs.plugins.buildlogic.android.application.jacoco)
    alias(libs.plugins.buildlogic.android.application.firebase)
    alias(libs.plugins.buildlogic.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.binh.core.example"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.binh.core.example"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "com.binh.core.testing.CustomTestRunner"
    }

    productFlavors {
        getByName(CustomProductFlavor.dev.name) {
            versionCode = 1
            versionName = "1.0.0"
        }

        getByName(CustomProductFlavor.stag.name) {
            versionCode = 1
            versionName = "1.0.0"
        }

        getByName(CustomProductFlavor.prod.name) {
            versionCode = 1
            versionName = "1.0.0"
        }
    }
}

dependencies {

    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.ui)

    // other app dependencies here
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.window.core)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // firebase
    platform(libs.firebase.bom)
    implementation(libs.firebase.cloud.messaging)
    implementation(libs.firebase.analytics)

    // worker
    implementation(libs.androidx.work.ktx)
    ksp(libs.hilt.ext.compiler)
    implementation(libs.hilt.ext.work)

    // image loading
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    implementation(projects.libs.imageCompressor)

    implementation(libs.androidx.tracing.ktx)
    implementation(libs.kotlinx.serialization.json)

    // testing
    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(libs.leakcanary.android)

    // local unit test
    testImplementation(projects.core.testing)
    testImplementation(libs.robolectric)

    // AndroidX Test - Instrumented testing
    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(projects.core.uiTestHiltManifest)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.compose.ui.test)
}

dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}