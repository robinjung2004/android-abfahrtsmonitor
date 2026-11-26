plugins {
    id("com.android.application") version "8.13.0" apply false
    id("org.jetbrains.kotlin.android") version "2.2.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20" apply false
}

tasks.register<Zip>("abgabeZip") {
    group = "Abgabe"
    description = "Packt den aktuellen Projektstand mit den Antworten für ILIAS."
    archiveFileName.set("Abfahrtsmonitor-Abgabe.zip")
    destinationDirectory.set(layout.buildDirectory.dir("abgabe"))
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    includeEmptyDirs = false
    from(projectDir) {
        include("README.md", "ANTWORTEN.md", "settings.gradle.kts", "build.gradle.kts", "gradle.properties")
        include("gradlew", "gradlew.bat", "gradle/wrapper/*", "app/build.gradle.kts", "app/src/**")
        exclude("**/.*/**", "**/build/**", "**/.env*", "**/local.properties", "**/keystore.properties",
            "**/*.jks", "**/*.keystore", "**/*.pem", "**/*.p12", "**/*.apk", "**/*.zip")
        eachFile {
            if (path == "gradlew") permissions { unix("rwxr-xr-x") }
        }
    }
}
