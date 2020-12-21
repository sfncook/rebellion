plugins {
    id(BuildPlugins.ANDROID_LIBRARY)
    id(BuildPlugins.KOTLIN_ANDROID)
    id(BuildPlugins.KOTLIN_KAPT)
}

android {
    compileSdkVersion(BuildAndroidConfig.COMPILE_SDK_VERSION)
}

dependencies {

    implementation(Dependencies.KOTLIN)
    implementation(Dependencies.CORE_KTX)
    implementation(Dependencies.APPCOMPAT)
    implementation(Dependencies.DAGGER)
    implementation(Dependencies.JUNIT)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("androidx.room:room-runtime:2.2.6")
    implementation("androidx.room:room-ktx:2.2.6")
    implementation("com.google.code.gson:gson:2.8.6")
    api("androidx.room:room-runtime:2.2.6")
    kapt(Dependencies.DAGGER_COMPILER)
    kapt("androidx.lifecycle:lifecycle-compiler:2.2.0")
    kapt("androidx.room:room-compiler:2.2.0")
}
