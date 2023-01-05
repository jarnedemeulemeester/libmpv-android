@Suppress("DSL_SCOPE_VIOLATION") // False positive
plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.jdtech.mpv"
    compileSdk = 33
    buildToolsVersion = "33.0.1"
    ndkVersion = "25.1.8937393"

    defaultConfig {
        minSdk = 26
        targetSdk = 33
        consumerProguardFiles("proguard-rules.pro")
        externalNativeBuild {
            cmake {
                arguments += "-DANDROID_STL=c++_shared"
                cFlags += "-Werror"
                cppFlags += "-std=c++11"
            }
        }

    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {
    implementation(libs.androidx.annotation)
}

extra.apply {
    set("PUBLISH_GROUP_ID", "dev.jdtech.mpv")
    set("PUBLISH_ARTIFACT_ID", "libmpv")
    set("PUBLISH_VERSION", "0.1.0")
}

apply {
    from("$rootDir/scripts/publish-module.gradle")
}