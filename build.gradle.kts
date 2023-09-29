import org.jetbrains.kotlin.cli.jvm.main

plugins {
    kotlin("jvm") version "1.9.10"
    id("application")
    id("edu.sc.seis.launch4j") version "3.0.5"
}

group = "overlayj"
version = "0.2.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
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
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.java.dev.jna:jna-platform:5.13.0")
    implementation("com.github.kwhat:jnativehook:2.2.2")
    implementation("cc.ekblad:4koma:1.2.0")
    implementation("com.formdev:flatlaf:3.2.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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

//tasks.named("test") {
//    useJUnitPlatform()
//}
