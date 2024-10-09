plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlinx.serialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}

kotlin {
    jvmToolchain(18)
}

//sourceSets {
//    val main by getting {
//        java.srcDirs("build/generated/ksp/main/kotlin")
//    }
//}
//ksp {
//    arg("room.schemaLocation", "$projectDir/schemas")
//}
dependencies {
    implementation (libs.gson)
    //implementation(libs.androidx.room.runtime.jvm)
    //Room
    //implementation(libs.androidx.room.runtime)
   //implementation(libs.androidx.room.runtime.jvm)
  //  ksp("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-common:2.6.1")  //used in non android libraries for room annotations
    // optional - Kotlin Extensions and Coroutines support for Room
   // implementation (libs.androidx.room.ktx)


    //Ktor for http requests
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation (libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.serialization)
    implementation (libs.ktor.client.content.negotiation)
}