import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.ksp)
    id("androidx.room") version "2.7.0"
}

loom {
    accessWidenerPath = file("src/main/resources/bluemapminimap.accesswidener")
}

room {
    schemaDirectory("$projectDir/schemas")
}

repositories {
    maven {
        name = "Ladysnake Mods"
        url = uri("https://maven.ladysnake.org/releases")
        content {
            includeGroup("io.github.ladysnake")
            includeGroup("org.ladysnake")
            includeGroupByRegex("dev\\.onyxstudios.*")
        }
    }
    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }
    maven { url = uri("https://maven.shedaniel.me/") }
    maven { url = uri("https://maven.terraformersmc.com/releases/") }
    maven {
        name = "CottonMC"
        url = uri("https://server.bbkr.space/artifactory/libs-release")
    }
    google {
        mavenContent {
            includeGroupAndSubgroups("androidx")
            includeGroupAndSubgroups("com.android")
            includeGroupAndSubgroups("com.google")
        }
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.loader)

    modImplementation(libs.fabric.api)

    modImplementation(libs.fabric.language.kotlin)

    shadow(libs.exposed.core)
    shadow(libs.exposed.jdbc)

    shadow(libs.sqlite.jdbc)

    implementation(libs.koin)
    shadow(libs.koin)

    shadow(libs.kotlinx.coroutines.jvm)
    shadow(libs.kotlinx.coroutines.swing)
    shadow(libs.kotlinx.coroutines)
    shadow(libs.kotlinx.serialization.json)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.contentnegotiation)
    implementation(libs.ktor.serialization.json)

    modImplementation(libs.renderer)
    include(libs.renderer)

    modApi(libs.clothconfig) {
        exclude(group = libs.fabric.api.get().group)
    }
    modApi(libs.modmenu)

    implementation(libs.ipaddress)

    implementation(libs.room.runtime)
    implementation(libs.room.gradle.plugin)
    implementation(libs.sqlite.bundled)
    ksp(libs.room.compiler)
}

val targetJavaVersion = 21

tasks.shadowJar {
    archiveVersion = ""
    configurations = listOf(project.configurations.shadow.get())
    archiveClassifier = "shadow"
    exclude("META-INF")
}

tasks.remapJar {
    dependsOn.add(tasks.shadowJar.get())
    inputFile.set(tasks.shadowJar.get().archiveFile.get())
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", libs.versions.minecraft.get())
    inputs.property("loader_version", libs.versions.fabric.loader.get())
    inputs.property("kotlin_loader_version", libs.versions.fabric.language.kotlin.get())
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to libs.versions.minecraft.get(),
            "loader_version" to libs.versions.fabric.loader.get(),
            "kotlin_loader_version" to libs.versions.fabric.language.kotlin.get()
        )
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"

    options.release.set(targetJavaVersion)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
}

version = project.property("mod_version") as String
group = project.property("maven_group") as String

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
}