import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecraftforge.gradle.user.IReobfuscator
import net.minecraftforge.gradle.user.ReobfMappingType.SEARGE
import net.minecraftforge.gradle.user.TaskSingleReobf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
    id("net.minecraftforge.gradle.forge") version "6f53277"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    id("org.spongepowered.mixin") version "0.6-SNAPSHOT"
    java
    idea
}

version = "0.1.3"
group = "skyblockclient"

minecraft {
    version = "1.8.9-11.15.1.2318-1.8.9"
    runDir = "run"
    mappings = "stable_22"
    makeObfSourceJar = false
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://jitpack.io")
    maven("https://maven.minecraftforge.net")
    maven("https://repo.spongepowered.org/repository/maven-public/")
    maven("https://repo.sk1er.club/repository/maven-public")
}

val packageLib: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

dependencies {
    annotationProcessor("org.spongepowered:mixin:0.7.11-SNAPSHOT")
    implementation("org.spongepowered:mixin:0.7.11-SNAPSHOT")
    packageLib("gg.essential:loader-launchwrapper:1.1.3")
    implementation("gg.essential:essential-1.8.9-forge:2666")
}

mixin {
    defaultObfuscationEnv = searge
    add(sourceSets.main.get(), "mixins.skyblockclient.refmap.json")
}

sourceSets {
    main {
        ext["refmap"] = "mixins.skyblockclient.refmap.json"
        output.setResourcesDir(file("${buildDir}/classes/kotlin/main"))
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        inputs.property("mcversion", project.minecraft.version)

        filesMatching("mcmod.info") {
            expand(mapOf("version" to project.version, "mcversion" to project.minecraft.version))
        }
    }
    named<Jar>("jar") {
        archiveBaseName.set("SkyblockClient")
        manifest.attributes(
            "FMLCorePluginContainsFMLMod" to true,
            "FMLCorePlugin" to "skyblockclient.forge.FMLLoadingPlugin",
            "ForceLoadAsMod" to true,
            "MixinConfigs" to "mixins.skyblockclient.json",
            "ModSide" to "CLIENT",
            "TweakClass" to "gg.essential.loader.stage0.EssentialSetupTweaker",
            "TweakOrder" to "0"
        )
        enabled = false
    }
    named<ShadowJar>("shadowJar") {
        archiveFileName.set(jar.get().archiveFileName)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        configurations = listOf(packageLib)
        mergeServiceFiles()
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    named<TaskSingleReobf>("reobfJar") {
        dependsOn(shadowJar)
    }
}

configure<NamedDomainObjectContainer<IReobfuscator>> {
    create("shadowJar") {
        mappingType = SEARGE
        classpath = sourceSets.main.get().compileClasspath
    }
}
