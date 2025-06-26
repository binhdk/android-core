/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import buildlogic.configureKotlinAndroid
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.gradle.AbstractAppExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "com.dropbox.dependency-guard")
            apply(plugin = "org.jetbrains.kotlin.plugin.parcelize")


            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                testOptions.animationsDisabled = true
                testOptions.unitTests.isIncludeAndroidResources = true

                packaging {
                    resources {
                        excludes.add("/META-INF/{AL2.0,LGPL2.1}")
                    }
                }
            }

            extensions.configure<AbstractAppExtension> {
                signingConfigs {
                    //  Create a variable called keystorePropertiesFile, and initialize it to your
                    //  keystore.properties file, in the rootProject folder.
                    val keystorePropertiesFile = rootProject.file("config/keystore.properties")
                    //  Initialize a new Properties() object called keystoreProperties.
                    val keystoreProperties = Properties()
                    //  Load your keystore.properties file into the keystoreProperties object.
                    keystoreProperties.load(FileInputStream(keystorePropertiesFile))

                    create("release") {
                        keyAlias = keystoreProperties.getProperty("keyAlias")
                        keyPassword = keystoreProperties.getProperty("keyPassword")
                        storeFile = file(keystoreProperties.getProperty("storeFile"))
                        storePassword = keystoreProperties.getProperty("storePassword")
                    }
                }

                buildTypes {
                    getByName("release") {
                        //  Enables code shrinking, obfuscation, and optimization for only
                        //  your project's release build type.
                        isMinifyEnabled = true

                        //  Enables resource shrinking, which is performed by the
                        //  Android Gradle plugin.
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                        signingConfig = signingConfigs.getByName("release")
                    }

                    getByName("debug") {
                        isDebuggable = true
                        signingConfig = signingConfigs.getByName("release")
                    }
                }

                applicationVariants.all {
                    val variant = this
                    variant.outputs
                        .map { it as BaseVariantOutputImpl }
                        .forEach { output ->
                            val versionName = variant.versionName
                            val versionCode = variant.versionCode
                            val buildVariant = variant.name
                            val date =
                                SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.US).format(Date())
                            output.outputFileName =
                                "${project.name}_v${versionName}(${versionCode})_${buildVariant}_${date}.apk"
                        }
                }
            }
            extensions.configure<ApplicationAndroidComponentsExtension> {
                //
            }
        }
    }

}
