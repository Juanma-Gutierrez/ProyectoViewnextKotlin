plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Kapt
    id("org.jetbrains.kotlin.kapt")
    // Hilt
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.viewnext.proyectoviewnext"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.viewnext.proyectoviewnext"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Fragments
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    // Navigation
    val navVersion = "2.7.6"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    // Toolbar
    implementation("androidx.activity:activity-ktx:1.8.2")
    // Retrofit
    val rfVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$rfVersion")
    implementation("com.squareup.retrofit2:converter-gson:$rfVersion")
    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // Dagger - Hilt - Kapt
    val kaptHiltVersion = "2.48.1"
    kapt("com.google.dagger:hilt-compiler:$kaptHiltVersion")
    implementation("com.google.dagger:hilt-android:$kaptHiltVersion")
    // Retromock
    implementation ("co.infinum:retromock:1.1.1")
    // Room
    val roomVersion = "2.6.1"
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // Lottie
    implementation ("com.airbnb.android:lottie:6.3.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}