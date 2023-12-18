plugins {
    `kotlin-dsl`

    id("dev.yumi.gradle.licenser") version "1.0.+"
}

repositories {
    mavenCentral()
}

license {
    rule(file("../codeformat/HEADER"))
    include("**/*.kt")
}
