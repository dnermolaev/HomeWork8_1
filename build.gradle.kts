import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
}

group = "dnermolaev"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation ("junit:junit:4.13.2")
}



tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}