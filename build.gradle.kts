import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.future"
version = "1.4.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

//                implementation(compose.desktop.currentOs) {
//                    exclude("org.jetbrains.compose.material")
//                }
//                implementation("com.bybutter.compose:compose-jetbrains-expui-theme:2.0.0")

                implementation("com.google.code.gson:gson:2.10.1")
                implementation("androidx.test.ext:junit-ktx:1.1.5")

//                implementation("com.alibaba.fastjson2:fastjson2-kotlin:2.0.29")

//                implementation("org.json:json:20230227")
//                implementation("com.github.wnameless.json:json-flattener:0.16.4")

                // https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
//                implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")

//                implementation("com.github.java-json-tools:json-schema-validator:2.2.14")


            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "JParser"
            packageVersion = "1.0.0"

            //modules("jdk.unsupported")
        }
    }
}
