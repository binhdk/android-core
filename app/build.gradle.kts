import buildlogic.CustomProductFlavor

plugins {
    alias(libs.plugins.buildlogic.android.application)
    alias(libs.plugins.buildlogic.android.application.flavors)
    alias(libs.plugins.buildlogic.android.application.jacoco)
    alias(libs.plugins.buildlogic.android.application.firebase)
    alias(libs.plugins.buildlogic.hilt)
    alias(libs.plugins.navigation.safeargs.kotlin)
}

android {
    namespace = "com.binh.core.example"

    buildFeatures {
        buildConfig = true
        viewBinding = true
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

    // firebase
    platform(libs.firebase.bom)
    implementation(libs.firebase.cloud.messaging)
    implementation(libs.firebase.analytics)

    // worker
    implementation(libs.androidx.work.ktx)
    ksp(libs.hilt.ext.compiler)
    implementation(libs.hilt.ext.work)

    // hilt-navigation
    api(libs.hilt.ext.navigation.fragment)

    // image loading
    implementation(libs.coil.kt)

    implementation(projects.libs.imageCompressor)

    // testing
    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(libs.leakcanary.android)

    // local unit test
    testImplementation(projects.core.testing)
    testImplementation(libs.robolectric)

    // AndroidX Test - Instrumented testing
    debugImplementation(projects.core.uiTestHiltManifest)
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.androidx.navigation.testing)
}

dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}