plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.maven.publish) apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
