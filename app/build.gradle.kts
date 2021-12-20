import java.util.Properties
import com.android.build.api.dsl.AndroidSourceSet

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

val composeVersion = "1.0.5"

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "io.eugenethedev.taigamobile"
        minSdk = 21
        targetSdk = 31
        versionCode = 19
        versionName = "1.6"
        project.base.archivesBaseName = "TaigaMobile-$versionName"

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
            val properties = Properties().also {
                it.load(file("./signing.properties").inputStream())
            }
            storeFile = file("./keystores/release.keystore")
            storePassword = properties.getProperty("password")
            keyAlias = properties.getProperty("alias")
            keyPassword = properties.getProperty("password")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

    lint { 
        isAbortOnError = false
    }

}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    implementation(kotlin("reflect"))

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.6.0-alpha01")

    // Main Compose dependencies
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    // Material You
    implementation("androidx.compose.material3:material3:1.0.0-alpha02")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.animation:animation:$composeVersion")
    // compose activity
    implementation("androidx.activity:activity-compose:1.4.0")
    // view model support
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0")
    // compose constraint layout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc02")

    // Accompanist
    val accompanistVersion = "0.18.0"
    implementation("com.google.accompanist:accompanist-pager:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-insets:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-flowlayout:$accompanistVersion")

    // Coil
    implementation("io.coil-kt:coil-compose:1.3.2")

    // Navigation Component (with Compose)
    implementation("androidx.navigation:navigation-compose:2.4.0-rc01")

    // Paging (with Compose)
    implementation("androidx.paging:paging-compose:1.0.0-alpha14")

    // Coroutines
    val coroutinesVersion = "1.5.2-native-mt"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    // Retrofit 2
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    // OkHttp
    val okHttpVersion = "4.9.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    // Dagger 2
    val daggerVersion = "2.40.5"
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Markdown support (Markwon)
    val markwonVersion = "4.6.2"
    implementation("io.noties.markwon:core:$markwonVersion")
    implementation("io.noties.markwon:image-coil:$markwonVersion")

    // Compose material dialogs (color picker)
    implementation("io.github.vanpra.compose-material-dialogs:color:0.6.1")

    /**
     * Test frameworks
     */
    allTestsImplementation(kotlin("test-junit"))

    // Robolectric (run android tests on local host)
    testRuntimeOnly("org.robolectric:robolectric:4.7.3")

    allTestsImplementation("androidx.test:core-ktx:1.4.0")
    allTestsImplementation("androidx.test:runner:1.4.0")
    allTestsImplementation("androidx.test.ext:junit-ktx:1.1.3")

    // since we need to connect to test db instance
    testRuntimeOnly("org.postgresql:postgresql:42.3.1")
    androidTestRuntimeOnly("org.postgresql:postgresql:42.3.1")

    // MockK
    testImplementation("io.mockk:mockk:1.12.1")
}

fun DependencyHandler.allTestsImplementation(dependencyNotation: Any) {
    testImplementation(dependencyNotation)
    androidTestImplementation(dependencyNotation)
}
