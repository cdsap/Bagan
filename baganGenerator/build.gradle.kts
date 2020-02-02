buildscript {
    repositories {
        jcenter()
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
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.7.3")
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    implementation("com.squareup.moshi:moshi-adapters:1.8.0")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.0")
    testImplementation("io.kotlintest:kotlintest-extensions:3.4.0")
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

