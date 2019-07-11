import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("com.novoda:bintray-release:0.9")
    }
}


plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    groovy
    kotlin("jvm")

}

group = "com.cdsap"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.11")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
}


tasks.register("convertFiles", TaskHeaderReplacer::class.java) {
    doLast {
        println("dkdkdk")
        showFile(project)

    }
}

val x = tasks.getByName("build") {
    this.dependsOn("convertFiles")
}
