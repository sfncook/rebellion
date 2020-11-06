plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    jcenter()
    mavenCentral()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

object PluginsVersions {
    const val GRADLE_ANDROID = "4.1.0"
    const val GRADLE_VERSIONS = "0.33.0"
    const val KOTLIN = "1.4.10"
    const val NAVIGATION = "2.3.0"
    const val JACOCO = "0.16.0"
    const val DOKKA = "0.10.0"
    const val KTLINT = "0.39.0"
    const val SPOTLESS = "5.6.1"
    const val DETEKT = "1.14.1"
    const val GRAPH_GENERATOR = "0.6.0-SNAPSHOT"
}

dependencies {
    implementation("com.android.tools.build:gradle:${PluginsVersions.GRADLE_ANDROID}")
    implementation("com.github.ben-manes:gradle-versions-plugin:${PluginsVersions.GRADLE_VERSIONS}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${PluginsVersions.KOTLIN}")
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:${PluginsVersions.NAVIGATION}")
}
