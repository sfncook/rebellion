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
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    kapt(Dependencies.DAGGER_COMPILER)
}
