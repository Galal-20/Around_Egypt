plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    //kotlin("jvm") version "1.9.0" // or the version you're using
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0" // make sure to match the version with Kotlin version

}

android {
    namespace = "com.galal.aroundegypt"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.galal.aroundegypt"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    //ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-compose-android:2.8.6")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")



    // Slider
    implementation("com.google.accompanist:accompanist-pager:0.36.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.32.0")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation ("androidx.datastore:datastore-preferences:1.0.0")  // For storing preferences
    implementation ("androidx.datastore:datastore:1.0.0" )            // For storing data objects
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    implementation ("com.google.code.gson:gson:2.8.9")


    //coil for images
    implementation("io.coil-kt:coil-compose:2.4.0")
    //Animation
    implementation("com.airbnb.android:lottie-compose:6.0.0")
    implementation("com.github.stevdza-san:OneTapCompose:1.0.14")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.31.2-alpha")

    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test:core:1.4.0")
    testImplementation("org.robolectric:robolectric:4.10")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.test.ext:junit:1.1.3")
    testImplementation("org.robolectric:robolectric:4.7.3")
    testImplementation ("org.mockito:mockito-core:3.12.4")
    testImplementation ("org.mockito:mockito-inline:3.12.4")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation ("app.cash.turbine:turbine:0.5.2")
    testImplementation ("org.mockito:mockito-inline:3.12.4")
    testImplementation ("io.mockk:mockk:1.13.5")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.0.0")

    testImplementation ("org.slf4j:slf4j-simple:1.7.32")
    testImplementation ("io.mockk:mockk:1.12.0")
    testImplementation ("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testImplementation ("org.junit.jupiter:junit-jupiter-engine:5.7.2")

    implementation ("androidx.room:room-runtime:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")
    implementation ("androidx.room:room-ktx:2.6.0")
}