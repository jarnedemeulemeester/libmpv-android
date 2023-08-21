plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
    signing
}

android {
    namespace = "dev.jdtech.mpv"
    compileSdk = 34
    buildToolsVersion = "34.0.0"
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

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("release") {
            groupId = "dev.jdtech.mpv"
            artifactId = "libmpv"
            version = "0.1.3"

            afterEvaluate {
                from(components["release"])
            }

            pom {
                name = "dev.jdtech.mpv"
                description = "libmpv for Android"
                url = "https://github.com/jarnedemeulemeester/libmpv-android"
                licenses {
                    license {
                        name = "MIT license"
                        url = "https://github.com/jarnedemeulemeester/libmpv-android/blob/main/LICENSE"
                    }
                }
                developers {
                    developer {
                        id = "jarnedemeulemeester"
                        name = "Jarne Demeulemeester"
                        email = "jarnedemeulemeester@gmail.com"
                    }
                }
                scm {
                    url = "https://github.com/jarnedemeulemeester/libmpv-android.git"
                    connection = "scm:git@github.com:jarnedemeulemeester/libmpv-android.git"
                    developerConnection = "scm:git@github.com:jarnedemeulemeester/libmpv-android.git"
                }
                issueManagement {
                    url = "https://github.com/jarnedemeulemeester/libmpv-android/issues"
                }
            }
        }
    }
}

configure<SigningExtension> {
    useInMemoryPgpKeys(
        rootProject.ext["signing.keyId"]?.toString(),
        rootProject.ext["signing.key"]?.toString(),
        rootProject.ext["signing.password"]?.toString(),
    )
    sign {
        extensions.getByType<PublishingExtension>().publications
    }
}