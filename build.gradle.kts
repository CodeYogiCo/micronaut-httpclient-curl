buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}


plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
    jacoco
    id("org.jetbrains.dokka") version "1.6.10" apply false
}
val targetJvm = 11


repositories {
    mavenLocal()
    mavenCentral()
}


dependencies {

    implementation(platform(libs.micronautBom))
    implementation(platform(libs.jacksonBom))
    implementation(libs.kotlinReflect)
    implementation(libs.kotlinStdlib)
    implementation(libs.bundles.micronaut)

    testImplementation(platform(libs.micronautBom))

    testImplementation(libs.kotlinStdlib)
    testImplementation(libs.kotlinReflect)
    testImplementation(libs.bundles.micronaut)

    testImplementation(libs.junitJupiterApi)
    testImplementation(libs.junitJupiterParams)
    testRuntimeOnly(libs.junitJupiterEngine)

    testImplementation(libs.bundles.micronautTest)

    testImplementation(libs.jacksonModuleKotlin)
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = targetJvm.toString()
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    test {
        useJUnitPlatform()
    }
}

