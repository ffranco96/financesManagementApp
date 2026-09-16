import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.example.financesmanagementapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.financesmanagementapp"
        minSdk = 26
        targetSdk = 35
        versionCode = (project.property("APP_VERSION_CODE") as String).toInt()
        versionName = project.property("APP_VERSION_NAME") as String

        // Output APK
        applicationVariants.all {
            val variant = this
            variant.outputs
                .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                .forEach { output ->
                    val appName = rootProject.name
                    val buildType = variant.buildType.name        // debug / release
                    val version = variant.versionName

                    output.outputFileName = "${appName}_v${version}_${buildType}.apk"
                }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Default for the firebase_crashlytics_collection_enabled / firebase_analytics_collection_enabled
        // manifest meta-data. Any build type without an explicit override inherits this (true);
        // `debug` overrides it to false below.
        manifestPlaceholders["crashlyticsCollectionEnabled"] = true
    }

    packaging {
        resources {
            pickFirsts += listOf(
                "META-INF/gradle/incremental.annotation.processors"
            )
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md"
            )
        }
    }

    lint {
        // NonNullableMutableLiveDataDetector throws IncompatibleClassChangeError against the Kotlin
        // Analysis API on this toolchain (AGP 8.7.3 / Kotlin 2.0) and aborts the whole lint task —
        // both `lintDebug` and the `lintVitalRelease` that gates release builds. It's a bug in lint,
        // not in this project's code. Re-enable after an AGP/lint upgrade.
        disable += "NullSafeMutableLiveData"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            // TODO - TEMPORARY: signs release with the auto-generated debug keystore so it's
            // installable for local testing (e.g. verifying Crashlytics). There is no real
            // release signing config yet
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // crashlyticsCollectionEnabled inherits true from defaultConfig.
        }
        debug {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    hilt {
        enableAggregatingTask = false
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.coroutines.android)
    implementation(libs.work.runtime)
    implementation(libs.work.test)
    implementation(libs.lifecycle)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    implementation(libs.androidx.compose.material.icons.extended)
    kapt(libs.hilt.compiler)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}