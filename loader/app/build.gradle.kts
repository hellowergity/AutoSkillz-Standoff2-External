plugins {
    alias(libs.plugins.android.application)
}

android {
    signingConfigs {
        create("main") {
            storeFile = file("${projectDir}/sign.jks")
            keyAlias = "wergity"
            storePassword = ""
            keyPassword = ""
        }
    }

    namespace = "com.wergity.autoskillzex"
    compileSdk = 36

    buildFeatures {
        buildConfig = false
    }

    defaultConfig {
        applicationId = "com.wergity.autoskillzex"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = false
        signingConfig = signingConfigs.getByName("main")
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
        }

        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.appcompat)
    implementation(libs.superuser)
    implementation(libs.fadingTextView)
    implementation(libs.constraintlayout)
}