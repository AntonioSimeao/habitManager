plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"

}

kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.compose.runtime:runtime:1.5.11")
                implementation("org.jetbrains.compose.foundation:foundation:1.5.11")
                implementation("org.jetbrains.compose.material3:material3:1.5.11")
                implementation(libs.material.icons.extended)
            }
        }

        val androidMain by getting {
            dependencies {
                val room_version = "2.7.1"
                implementation("androidx.activity:activity-compose:1.8.0")
                implementation("androidx.room:room-runtime:$room_version")
                implementation("androidx.room:room-ktx:$room_version")
            }
        }
    }
}


android {
    namespace = "com.app.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}