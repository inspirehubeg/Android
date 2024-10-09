
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
}

android {
    namespace = "alexschool.bookreader"
    compileSdk = 34

    defaultConfig {
        applicationId = "alexschool.bookreader"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
       // buildConfigField("String","API_KEY","API_KEY")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    androidResources {
        generateLocaleConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = "18"
    }
    buildFeatures {
        compose = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.1"
//    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(project(":readingPad"))
    implementation(project(":network"))
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation (libs.gson)

    //Dagger-Hilt
    implementation(libs.dagger.hilt)
    ksp ("com.google.dagger:hilt-compiler:2.51.1")
    implementation(libs.hilt.navigation.compose)

    //Coil for image handling
    implementation(libs.coil.compose)

    //Ktor for http requests
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation (libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation (libs.ktor.client.content.negotiation)


   // Room
    implementation(libs.androidx.room.runtime)
    ksp("androidx.room:room-compiler:2.6.1")


   // implementation(libs.androidx.room.compiler) //for kapt not ksp
//    kapt (libs.androidx.room.compiler){
//        exclude(group = "com.intellij", module = "annotations") // Add this line
//    }

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation (libs.androidx.room.ktx)

}