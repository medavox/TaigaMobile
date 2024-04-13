import com.android.build.api.dsl.AndroidSourceSet

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "1.8.22"
}

val composeVersion = "1.1.1"

android {
    compileSdk = 34

    namespace = "io.eugenethedev.taigamobile"

    defaultConfig {
        applicationId = namespace!!
        minSdk = 21
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 31
        versionCode = 29
        versionName = "1.9"
        project.base.archivesName.set("TaigaMobile-$versionName")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("./keystores/debug.keystore")
            storePassword = "android"
            keyAlias = "debug"
            keyPassword = "android"
        }

        create("release") {
            storeFile = file("./keystores/release.keystore")
            storePassword = properties["password"] as String?
            keyAlias = properties.get("alias") as String?
            keyPassword = properties.get("password") as String?
        }
    }


    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
    }

    sourceSets {
        fun AndroidSourceSet.setupTestSrcDirs() {
            kotlin.srcDir("src/sharedTest/kotlin")
            resources.srcDir("src/sharedTest/resources")
        }

        getByName("test").setupTestSrcDirs()
        getByName("androidTest").setupTestSrcDirs()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }

    lint { 
        abortOnError = false
    }
}

dependencies {
    // Enforce correct kotlin version for all dependencies
    implementation(enforcedPlatform(kotlin("bom")))

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation(kotlin("reflect"))

    implementation(libs.androidx.core.ktx.v170)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // Main Compose dependencies
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    // Material You
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.animation:animation:$composeVersion")
    // compose activity
    implementation(libs.androidx.activity.compose)
    // view model support
    implementation(libs.androidx.lifecycle.viewmodel.compose.v241)
    // compose constraint layout
    implementation(libs.androidx.constraintlayout.compose)

    // Accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.flowlayout)

    // Coil
    implementation(libs.coil.compose)

    // Navigation Component (with Compose)
    implementation(libs.androidx.navigation.compose)

    // Paging (with Compose)
    implementation(libs.androidx.paging.compose)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)

    // Retrofit 2
    implementation(libs.retrofit)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Timber
    implementation(libs.timber)

    // Markdown support (Markwon)
    implementation(libs.markwon)
    implementation(libs.markwon.image.coil)

    // Compose material dialogs (color picker)
    implementation(libs.color)

    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")


    /**
     * Test frameworks & dependencies
     */
    allTestsImplementation(kotlin("test-junit"))

    // Robolectric (run android tests on local host)
    testRuntimeOnly(libs.robolectric)

    allTestsImplementation(libs.test.core.ktx)
    allTestsImplementation(libs.androidx.runner)
    allTestsImplementation(libs.androidx.junit.ktx)

    // since we need to connect to test db instance
    val postgresDriverVersion = "42.3.6"
    testRuntimeOnly(libs.postgresql)
    androidTestRuntimeOnly(libs.postgresql)

    // manual json parsing when filling test instance
    implementation(libs.gson)

    // MockK
    testImplementation(libs.mockk)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}


fun DependencyHandler.allTestsImplementation(dependencyNotation: Any) {
    testImplementation(dependencyNotation)
    androidTestImplementation(dependencyNotation)
}

tasks.register<Exec>("launchTestInstance") {
    commandLine("../taiga-test-instance/launch-taiga.sh")
}

tasks.register<Exec>("stopTestInstance") {
    commandLine("../taiga-test-instance/stop-taiga.sh")
}

tasks.withType<Test> {
    dependsOn("launchTestInstance")
    finalizedBy("stopTestInstance")
}

