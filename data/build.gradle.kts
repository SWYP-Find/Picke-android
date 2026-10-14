import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.picke.data"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val properties = Properties()
    val propertiesFile = project.rootProject.file("local.properties")
    if (propertiesFile.exists()) {
        properties.load(propertiesFile.inputStream())
    }

    val baseUrlDebug = properties.getProperty("BASE_URL_DEBUG")
    val baseUrlRelease = properties.getProperty("BASE_URL_RELEASE")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    buildTypes {
        release {
            buildConfigField("String", "BASE_URL", "\"$baseUrlRelease\"")
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"$baseUrlDebug\"")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))

    // [DI - Hilt]
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // [Network - Retrofit]
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // [Security]
    implementation(libs.androidx.security.crypto)

    // [Sentry]
    implementation(libs.sentry)

    // [Paging3]
    implementation(libs.androidx.paging.runtime)
}