@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.service)
//    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.global.appsarthi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.global.appsarthi"
        minSdk = 24
        targetSdk = 34
        versionCode = 180
        versionName = "1.8.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.coil.compose)
    //constraint layout
    implementation(libs.androidx.constraintlayout.compose)
    //Firebase
    implementation (libs.firebase.messaging.ktx)
    implementation(libs.play.services.auth)
    implementation (libs.play.services.location)
    //viewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //DataStore
    implementation(libs.androidx.datastore.preferences)
    //Coroutine
    implementation(libs.kotlinx.coroutines.android)
    //Google GMS
    implementation (libs.play.services.location)
    implementation (libs.firebase.messaging.ktx)
    implementation(libs.play.services.auth)

    //Permission
    implementation (libs.accompanist.permissions)
    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    //Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.compiler)
    ksp(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)

//    implementation(libs.symbol.processing.api)
    ksp(libs.hilt.android.compiler)
    //icons
    implementation(libs.androidx.material.icons.extended.android)
    //WorkManager
    implementation(libs.androidx.work.runtime.ktx)
}