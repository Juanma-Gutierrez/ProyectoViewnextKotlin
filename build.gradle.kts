// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    // Kapt
    id("org.jetbrains.kotlin.kapt") version "2.0.0-Beta1" apply false
    // Hilt
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
}