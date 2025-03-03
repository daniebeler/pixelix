plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ktorfit)
}

kotlin {
    jvmToolchain(17)
    androidTarget()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //compose
            implementation(compose.ui)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(libs.compose.ui.graphics)

            //logger
            implementation(libs.kermit)

            //html parser
            implementation(libs.ksoup)

            //kotlinx
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections.immutable)

            //ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.logging)

            //ktorfit
            implementation(libs.ktorfit)
            implementation(libs.ktorfit.call)

            //DI
            implementation(libs.kotlin.inject.runtime)

            //datastore
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)

            //shared preferences
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.multiplatform.settings.datastore)

            //file picker
            implementation(libs.filekit.compose)

            //lifecycle
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.savedstate)

            //navigation
            implementation(libs.androidx.navigation.compose)

            //annotation
            implementation(libs.androidx.annotation)

            //disk io
            implementation(libs.okio)

            //image loader
            implementation(libs.coil.compose)
            implementation(libs.coil.video)
            implementation(libs.coil.gif)
            implementation(libs.coil.network)

            //image crop
            implementation(libs.krop)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)

            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)

            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.runtime.livedata)
            implementation(libs.androidx.browser)

            implementation(libs.accompanist.systemuicontroller)

            implementation(libs.material)

            //media
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.dash)
            implementation(libs.androidx.media3.ui)
            implementation(libs.android.image.cropper)

            // widget
            implementation(libs.androidx.glance.appwidget)
            implementation(libs.androidx.glance.material3)
            // work Manager
            implementation(libs.androidx.work.runtime.ktx)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.daniebeler.pfpixelix"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.daniebeler.pfpixelix"
        minSdk = 26
        targetSdk = 35
        versionCode = 27
        versionName = "4.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
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

        create("demo") {
            initWith(getByName("debug"))
            isMinifyEnabled = true
            isDebuggable = false
            isProfileable = false
            isShrinkResources = true
        }
    }
    packaging.resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    listOf(
        "kspAndroid",
        "kspIosX64",
        "kspIosArm64",
        "kspIosSimulatorArm64"
    ).forEach {
        add(it, libs.kotlin.inject.compiler.ksp)
    }
}
