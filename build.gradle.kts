import gay.pyrrha.wingit.Versions
import gay.pyrrha.wingit.base64Decode
import gay.pyrrha.wingit.isModAlpha
import gay.pyrrha.wingit.isModBeta
import gay.pyrrha.wingit.isModStable
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.date
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.21"
    id("fabric-loom") version "1.4.+"

    id("dev.yumi.gradle.licenser") version "1.0.+"
    id("org.jetbrains.changelog") version "2.2.0"
    id("com.modrinth.minotaur") version "2.8.4"
    id("com.github.jakemarsden.git-hooks") version "0.0.2"

    signing
    `maven-publish`
}

group = "gay.pyrrha"
version = "1.0.0"

dependencies {
    minecraft("com.mojang:minecraft:${Versions.MINECRAFT}")
    mappings("net.fabricmc:yarn:${Versions.YARN_MAPPINGS}:v2")
    modImplementation("net.fabricmc:fabric-loader:${Versions.FABRIC_LOADER}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${Versions.FABRIC_API}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${Versions.FABRIC_KOTLIN}")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }

        // https://stackoverflow.com/questions/41028030/gradle-minimize-json-resources-in-processresources#41029113
        doLast {
            // minify json for release builds
            if(isModStable()) {
                fileTree(mapOf(
                    "dir" to outputs.files.asPath,
                    "includes" to listOf("**/*.json", "**/*.mcmeta")
                )).forEach {
                    it.writeText(JsonOutput.toJson(JsonSlurper().parse(it)))
                }
            }
        }
    }

    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${project.archivesName.get()}" }
        }
    }

    test {
        useJUnitPlatform()
    }
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("wingit") {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("client"))
        }
    }
}

fabricApi {
    configureDataGeneration()
}

license {
    rule(file("codeformat/HEADER"))
    include("**/*.kt")
    include("**/*.java")
}

gitHooks {
    setHooks(mapOf("pre-commit" to "build checkLicenses buildSrc:checkLicenses"))
}

signing {
    sign(tasks.getByName("remapJar"))
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(
        signingKeyId,
        signingKey.base64Decode(),
        signingPassword.base64Decode()
    )

    isRequired = System.getenv("CI") != null && System.getenv("GITHUB_JOB") == "build-push"
}

changelog {
    version.set(project.version.toString())
    path.set(file("CHANGELOG.md").canonicalPath)
    header.set("[${project.version}] - ${date()}")
    headerParserRegex.set("""^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(?:-((?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\+([0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?${'$'}""")
    introduction.set("Wing It")
    itemPrefix.set("-")
    keepUnreleasedSection.set(true)
    unreleasedTerm.set("[Unreleased]")
    groups.set(listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security"))
    lineSeparator.set("\n")
    combinePreReleases.set(true)
}

val signingZipTask = tasks.create("signingZip", Zip::class.java) {
    dependsOn(tasks.findByName("signRemapJar"))

    from(tasks.findByName("signRemapJar")!!.outputs.files)
    from(files("src/main/resources/fabric.mod.json"))
    archivesName.set("${rootProject.name}-${project.version}-signature.zip")
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set("wing-it")
    versionNumber.set(project.version.toString())
    versionType.set(
        if (isModAlpha()) {
            "alpha"
        } else if (isModBeta()) {
            "beta"
        } else {
            "release"
        }
    )
    uploadFile.set(tasks.getByName("remapJar"))
    additionalFiles.addAll(tasks.getByName("remapSourcesJar"), signingZipTask)
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
    }
    changelog.set(provider {
        project.changelog.renderItem(
            project.changelog.getUnreleased()
                .withHeader(false)
                .withEmptySections(false),
            Changelog.OutputType.MARKDOWN
        )
    })
    syncBodyFrom.set(file("README.md").readText())
}

