plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.publish.plugin) apply true
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.wrapper {
    gradleVersion = "8.2.1"
    distributionType = Wrapper.DistributionType.ALL
}

apply {
    from("$rootDir/scripts/publish-root.gradle")
}
