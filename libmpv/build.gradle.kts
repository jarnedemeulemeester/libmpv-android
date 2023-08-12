plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "dev.jdtech.mpv"
    compileSdk = 33
    buildToolsVersion = "33.0.2"
    ndkVersion = "25.2.9519653"

    defaultConfig {
        minSdk = 26
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.annotation)
}

extra.apply {
    set("PUBLISH_GROUP_ID", "dev.jdtech.mpv")
    set("PUBLISH_ARTIFACT_ID", "libmpv")
    set("PUBLISH_VERSION", "0.1.2")
}

apply {
    from("$rootDir/scripts/publish-module.gradle")
}
