buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    groovy
    kotlin("jvm")

}

repositories {
    mavenCentral()
}

dependencies {
  //  implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    implementation("com.squareup.moshi:moshi-adapters:1.8.0")
    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.kotest:kotest-extensions:4.6.1")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
}


tasks.register("convertFiles", TaskHeaderReplacer::class.java) {
    input.set(File("$projectDir/src/main/java"))
    output.set(File("$buildDir"))

}
tasks.getByName("build") {
    this.dependsOn("convertFiles")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

