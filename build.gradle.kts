plugins {
    id("fabric-loom") version "1.4.+"
}

group = "com.mentionbot"
version = "1.0.0"
tasks.withType<Jar> {
    archiveBaseName.set("mention-bot")
}
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenCentral()
    maven { name = "FabricMC"; url = uri("https://maven.fabricmc.net/") }
}

dependencies {
    minecraft("com.mojang:minecraft:1.21.5")
    mappings("net.fabricmc:yarn:1.21.5+build.1:v2")
    modImplementation("net.fabricmc:fabric-loader:0.14.9")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.77.0+1.21.5")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
}
