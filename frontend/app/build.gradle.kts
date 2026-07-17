plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.VetPet"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.VetPet"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
//    implementation(libs.firebase.messaging.ktx) // Đã xóa: Google đã khai tử bản -ktx, chuyển sang dùng bản gốc kết hợp Firebase BoM.
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //sdp-ssp
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    //navigation
    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    //api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //room
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:...") //coroutine
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //JsonParser
    implementation("com.google.code.gson:gson:2.10.1")

    //Google Play Services Location
    implementation("com.google.android.gms:play-services-location:21.1.0")

    //thư viện lấy ảnh mạng
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Thư viện Calendar (phiên bản dành cho Android View)
    implementation("com.kizitonwose.calendar:view:2.10.1")

    // Hỗ trợ Java 8 API cho các thiết bị Android cũ (minSdk < 26)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    //Khai báo Firebase BoM (Quản lý phiên bản tự động)
    implementation(platform("com.google.firebase:firebase-bom:34.16.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
}