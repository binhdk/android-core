import buildlogic.CustomProductFlavor

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.buildlogic.android.library)
    alias(libs.plugins.buildlogic.android.library.jacoco)
    alias(libs.plugins.buildlogic.android.room)
    alias(libs.plugins.buildlogic.hilt)
}

android {
    namespace = "com.binh.core.data"

    buildFeatures {
        buildConfig = true
    }

    productFlavors {
        getByName(CustomProductFlavor.dev.name) {
            buildConfigField("String", "BASE_URL", "\"https://192.168.0.102:5000/api/\"")
        }

        getByName(CustomProductFlavor.stag.name) {
            buildConfigField("String", "BASE_URL", "\"https://192.168.0.102:5000/api/\"")
        }

        getByName(CustomProductFlavor.prod.name) {
            buildConfigField("String", "BASE_URL", "\"https://192.168.0.102:5000/api/\"")
        }
    }
}

dependencies {

    api(projects.core.common)

    // http client
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp.logging)

    // jwt
    implementation(libs.jwtdecode)

    // local data store
    api(libs.androidx.dataStore.preferences)


    // json serialize
    api(libs.moshi)
    ksp(libs.moshi.codegen)

    // security
    implementation(libs.androidx.security.crypto)


    testImplementation(projects.core.testing)

}