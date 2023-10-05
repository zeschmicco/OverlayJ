group = "overlayj"
version = "0.2.0-SNAPSHOT"

plugins {
    application

    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"

    id("edu.sc.seis.launch4j") version "3.0.5"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("overlayj.AppKt")
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

    implementation("org.greenrobot:eventbus-java:3.3.1")

    implementation("com.formdev:flatlaf:3.2.1")
    implementation("com.formdev:flatlaf-extras:3.2.1")
    implementation("com.formdev:flatlaf-fonts-inter:3.19")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
    val dependencies = configurations.runtimeClasspath.get().map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Zip>("releaseZip") {
    group = "distribution"

    archiveFileName.set("${project.name}-${project.version}.zip")
    destinationDirectory.set(layout.buildDirectory.dir("dist"))

    from(tasks.createExe)
    from(layout.projectDirectory.file("overlayj.json"))

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}