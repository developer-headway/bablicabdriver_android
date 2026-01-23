plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "com.headway.bablicabdriver"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.headway.bablicabdriver"
        minSdk = 29
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //navigation
    implementation(libs.androidx.navigation.compose)

    //lottie animation library
    implementation(libs.lottie.compose) // usage confirmed

    //lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)


    // lazy loading of images
    implementation(libs.coil.compose)

    //shadow
    implementation(libs.neumorphic)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.gson.converter)
    implementation(libs.okhttp3)
    implementation(libs.logging.interceptor)


    //gson file
    implementation (libs.gson)


    //image zooming library
    implementation(libs.zoomable)

    //pager indicator
    implementation(libs.compose.pager.indicator)

    //image cropper
    implementation(libs.android.image.cropper)

    //custom pull to refresh
    implementation(libs.pullrefresh)

    //google map
    implementation(libs.maps.compose)

    //location providers
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.maps.android:maps-utils-ktx:5.2.0")


    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.16.0")) // Use the latest version
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-messaging")


    //qr generator
    implementation("com.lightspark:compose-qr-code:1.0.1")

}