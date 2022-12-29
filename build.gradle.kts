buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.wrapper {
    gradleVersion = "7.6"
    distributionType = Wrapper.DistributionType.ALL
}
