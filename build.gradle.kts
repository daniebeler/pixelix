// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath (libs.kotlin.serialization)
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.hilt) apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.22" apply false
    alias(libs.plugins.compose.compiler) apply false
}