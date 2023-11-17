plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.0"
    kotlin("plugin.allopen") version "1.9.0"
}

dependencies {
    implementation(project(":web:group-chat-subdomain"))
    implementation(project(":web:private-chat-subdomain"))
    implementation(project(":web:user-subdomain"))
    implementation(project(":web:util"))
    implementation(project(":web:message-subdomain"))

    implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.1.5")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.1.5")

    implementation("org.springframework.kafka:spring-kafka:3.0.12")
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.21")
    implementation("io.confluent:kafka-schema-registry-maven-plugin:7.5.1")
    implementation("io.confluent:kafka-protobuf-serializer:7.5.1")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")

    implementation("net.devh:grpc-spring-boot-starter:2.15.0.RELEASE")
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.nats:jnats:2.16.14")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test:3.5.11")
    testImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
    testImplementation("org.awaitility:awaitility:4.1.1")
}

subprojects {

    apply(plugin = "kotlin")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.allopen")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {

        implementation(project(":internal-api"))

        implementation("com.sun.mail:jakarta.mail:2.0.1")
        implementation("org.springframework.boot:spring-boot-starter-mail:1.2.0.RELEASE")
        implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.5")
        implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")
        implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.1.5")
        implementation("org.springframework.boot:spring-boot-starter-data-redis:3.1.5")

        implementation("org.springframework.kafka:spring-kafka:3.0.12")
        implementation("io.projectreactor.kafka:reactor-kafka:1.3.21")
        implementation("io.confluent:kafka-schema-registry-maven-plugin:7.5.1")
        implementation("io.confluent:kafka-protobuf-serializer:7.5.1")

        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")

        implementation("net.devh:grpc-spring-boot-starter:2.15.0.RELEASE")
        implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("io.nats:jnats:2.16.14")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("io.projectreactor:reactor-test:3.5.11")
        testImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
    }

    tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        enabled = false
    }

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        enabled = false
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
