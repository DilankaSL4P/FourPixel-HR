plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.fourpixel.fourpixelhrapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fourpixel.fourpixelhrapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/INDEX.LIST",
                "META-INF/DEPENDENCIES",
                "META-INF/io.netty.versions.properties"
            )
            pickFirsts += setOf(
                "google/api/source_info.proto",
                "google/api/logging.proto"
            )
        }
    }
}

dependencies {
    // Core AndroidX
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    implementation("androidx.compose.foundation:foundation:1.6.0")
    implementation("androidx.compose.material:material:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.material:material-icons-core:1.6.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Firebase (via BoM)
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-ktx") {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")

    // Google Play Services
    implementation("com.google.android.gms:play-services-gcm:17.0.0")
    implementation("com.google.android.gms:play-services-tasks:18.0.2")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Ktor
    implementation("io.ktor:ktor-client-core:2.0.0")
    implementation("io.ktor:ktor-client-android:2.0.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.0.0")
    implementation("io.ktor:ktor-client-logging:2.0.0")
    implementation("io.ktor:ktor-client-serialization:2.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")

    // Serialization & Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.0")

    // Coil (Image Loading)
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Accompanist
    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")

    // Biometric
    implementation("androidx.biometric:biometric:1.1.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
}

configurations.all {
    resolutionStrategy {
        force("com.google.protobuf:protobuf-java:3.25.5")
    }
}