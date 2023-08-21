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

// Create variables with empty default values
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.key"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null
ext["sonatypeStagingProfileId"] = null

val secretPropsFile: File = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    // Read local.properties file first if it exists
    secretPropsFile.reader().use {
        java.util.Properties().apply {
            load(it)
        }
    }.onEach { (name, value) -> 
        ext[name.toString()] = value
    }
} else {
    // Use system environment variables
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
    ext["sonatypeStagingProfileId"] = System.getenv("SONATYPE_STAGING_PROFILE_ID")
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.key"] = System.getenv("SIGNING_KEY")
}

fun getExtraString(name: String) = ext[name]?.toString()

nexusPublishing {
    this.repositories {
        sonatype {
            stagingProfileId = getExtraString("sonatypeStagingProfileId")
            username = getExtraString("ossrhUsername")
            password = getExtraString("ossrhPassword")
            nexusUrl = uri("https://s01.oss.sonatype.org/service/local/")
            snapshotRepositoryUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}