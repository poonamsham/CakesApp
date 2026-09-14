import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    id("jacoco")
}

android {
    namespace = "com.example.cakes"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.cakes"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        buildFeatures {
            buildConfig = true
        }
// product flavors can be added here for diffrent environments
        buildConfigField(
            "String",
            "BASE_URL",
            "\"https://raw.githubusercontent.com/\"" // keeping same
        )
        testInstrumentationRunner = "com.example.cakes.HiltTestRunner"
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }

    lint {
        abortOnError = true
        checkReleaseBuilds = true
        warningsAsErrors = false
        baseline = file("lint-baseline.xml")
        disable += listOf("TypographyFractions", "TypographyQuotes")
        enable += listOf("RtlHardcoded", "RtlCompat", "RtlEnabled")
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "11"
}
tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {

    dependsOn("jacocoTestReport")

    violationRules {
        rule {
            limit {
                minimum = "0.0".toBigDecimal()
            }
        }
    }
}
detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(files("${rootProject.projectDir}/config/detekt/detekt.yml"))
    baseline = file("${rootProject.projectDir}/config/detekt/baseline.xml")
    buildUponDefaultConfig = true
}

ktlint {
    android = true
    ignoreFailures = false
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
}

configure<JacocoPluginExtension> {
    toolVersion = "0.8.12"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.matching { it.name == "testDebugUnitTest" || it.name == "connectedDebugAndroidTest" })

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*",
        "**/*Test*.*", "android/**/*.*", "**/*_HiltModules*", "**/*_Factory*",
        "**/*_MembersInjector*", "**/*Module_*", "com/example/cakes/ui/theme/**"
    )

    val debugTree = fileTree("${layout.buildDirectory.get()}/intermediates/built_in_kotlinc/debug/compileDebugKotlin/classes") {
        exclude(fileFilter)
    }

    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(layout.buildDirectory.get()) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        include("outputs/code_coverage/debugAndroidTest/connected/**/*.ec")
    })
}


dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("androidx.compose.material:material:1.12.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.12.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("com.google.dagger:hilt-android:2.57.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.dagger:hilt-android:2.57.1")
    implementation(libs.androidx.ui)
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockkAndroid)
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.57.1")
    kspAndroidTest("com.google.dagger:hilt-android-compiler:2.57.1")
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
}
