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
    testCompile("junit", "junit", "4.12")
}


//
//tasks.withType<TaskHeaderReplacer> {
//    dependsOn(tasks.getByPath(":compileKotlin"))
//    println("dkdkdk")
//    showFile()
//}

tasks.register("aaaaa", TaskHeaderReplacer::class.java) {
    // this.input = ""
    doLast {
        println("dkdkdk")
        showFile(project)

    }
}

val x = tasks.getByName("build") {
    this.dependsOn("aaaaa")
}
