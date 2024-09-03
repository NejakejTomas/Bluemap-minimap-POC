import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("fabric-loom") version "1.7.1"
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "2.0.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
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
}

dependencies {
    //to change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:1.21.1")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.2")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.102.1+1.21.1")

    modImplementation("net.fabricmc:fabric-language-kotlin:1.12.0+kotlin.2.0.10")

    include(implementation("org.jetbrains.exposed:exposed-core:0.47.0")!!)
    include(implementation("org.jetbrains.exposed:exposed-jdbc:0.47.0")!!)

    include(implementation("org.xerial:sqlite-jdbc:3.45.1.0")!!)

    shadow(implementation("io.insert-koin:koin-core:3.6.0-wasm-alpha2")!!)

    shadow(implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")!!)

    shadow(implementation("io.ktor:ktor-client-core-jvm:3.0.0-beta-1")!!)
    shadow(implementation("io.ktor:ktor-client-cio-jvm:3.0.0-beta-1")!!)
    shadow(implementation("io.ktor:ktor-client-content-negotiation:3.0.0-beta-1")!!)
    shadow(implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0-beta-1")!!)

    include(modImplementation("com.github.0x3C50:Renderer:master-SNAPSHOT")!!)

    modApi("me.shedaniel.cloth:cloth-config-fabric:15.0.130") {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi("com.terraformersmc:modmenu:11.0.1")

    include(implementation("com.github.seancfoley:ipaddress:5.5.0")!!)
    modImplementation(include("io.github.cottonmc:LibGui:11.1.0+1.21")!!)
}

val targetJavaVersion = 21

tasks.shadowJar {
    configurations = listOf(project.configurations.shadow.get())
    archiveClassifier = "shadow"
    exclude("META-INF")

    val prefix = "cz.nejakejtomas.bluemapminimap"

    setOf(
        "io.insert-koin",
        "org.jetbrains.kotlinx",
        "io.ktor",
        "com.github.seancfoley"
    ).forEach { pack ->
        relocate(pack, "$prefix.$pack")
    }
}

tasks.remapJar {
    dependsOn.add(tasks.shadowJar)
    mustRunAfter(tasks.shadowJar)
    inputFile.set(tasks.shadowJar.get().archiveFile.get())
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", project.property("minecraft_version"))
    inputs.property("loader_version", project.property("loader_version"))
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to project.property("minecraft_version"),
            "loader_version" to project.property("loader_version"),
            "kotlin_loader_version" to project.property("kotlin_loader_version")
        )
    }
}

// ensure that the encoding is set to UTF-8, no matter what the system default is
// this fixes some edge cases with special characters not displaying correctly
// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
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
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName}" }
    }
}