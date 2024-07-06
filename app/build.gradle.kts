plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    alias(libs.plugins.compose.compiler)
}


android {
    namespace = "com.daniebeler.pfpixelix"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.daniebeler.pfpixelix"
        minSdk = 26
        targetSdk = 34
        versionCode = 13
        versionName = "2.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.lifecycle.runtime.compose)


    implementation(libs.volley)
    implementation(libs.gson)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    implementation(libs.androidx.runtime.livedata)


    implementation(libs.coil.compose)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.material3)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.browser)

    implementation(libs.androidx.material.icons.extended)

    implementation(libs.accompanist.systemuicontroller)


    implementation(libs.material)

    implementation(libs.androidx.material)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.glide)
    ksp(libs.compiler)
    implementation(libs.compose)

    implementation(libs.coil.video)

    implementation(libs.android.image.cropper)

    implementation(libs.zoomable)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.lifecycle.runtime.ktx)

    // Saved state module for ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    // Annotation processor
    annotationProcessor(libs.androidx.lifecycle.compiler)

    // widget
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)
    // work Manager
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)

    implementation(libs.hilt.naviation)
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)

}
