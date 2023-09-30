group = "overlayj"
version = "0.2.0"

plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    id("application")
    id("java")
    id("edu.sc.seis.launch4j") version "3.0.5"
    id("com.github.ben-manes.versions") version "0.48.0"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("overlayj.App")
}

launch4j {
    mainClassName.set(application.mainClass)
    icon.set("${projectDir}/icon.ico")
    setJarTask(tasks.jar.get())
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")
    implementation("com.github.kwhat:jnativehook:2.2.2")
    implementation("com.formdev:flatlaf:3.2.1")
    implementation("com.github.weisj:darklaf-core:3.0.2")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
}
