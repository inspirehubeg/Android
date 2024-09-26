// Top-level build file where you can add configuration options common to all sub-projects/modules.
//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        classpath (libs.gradle)
//        classpath (libs.kotlin.gradle.plugin)
//
//        // Add other classpath dependencies as needed
//    }
//}
//
//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }
//}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hiltAndroid) apply false
   // id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}