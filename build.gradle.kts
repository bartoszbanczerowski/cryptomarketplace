// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }

    dependencies {
        classpath(libs.hiltgradle)
//        classpath(libs.oss.licenses.plugin)
//        classpath(libs.gradle)
//        classpath(libs.google.services)
//        classpath(libs.firebase.crashlytics.gradle)
//        classpath(libs.kotlin.gradle)
    }
}