plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hiltAndroid)
    id("kotlin-kapt")
}

android {
    namespace = "ih.tools.readingpad"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true // Add this line!
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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
    implementation(libs.androidx.recyclerview)
    implementation("androidx.compose.material:material-icons-extended:$1.4.3")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runtime.android)
    implementation(libs.engage.core)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.gson)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Dagger-Hilt
    implementation(libs.dagger.hilt)
    "kapt"(libs.dagger.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
//    //Room
    implementation(libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler){
        exclude(group = "com.intellij", module = "annotations") // Add this line
    }
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation (libs.androidx.room.ktx)
    //Coil for image handling
    implementation(libs.coil.compose)

    //colorPicker

    implementation(libs.compose.colorpicker)

    //Apache POI for word doc
    implementation (libs.poi.ooxml)

    //Ktor for http requests
//    implementation(libs.ktor.client.core)
//    implementation(libs.ktor.client.okhttp)
//    implementation(libs.ktor.client.android)
//    //implementation(libs.ktor.client.serialization.jvm)
//    implementation(libs.ktor.client.logging)
//    implementation (libs.ktor.client.content.negotiation)
}