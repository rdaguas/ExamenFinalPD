plugins {
    id("java")
    id("io.freefair.lombok") version "8.13.1"
    id("io.quarkus") version "3.22.2"
}

group = "com.progra.distribuida"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val quarkusVersion = "3.22.2"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:${quarkusVersion}"))

    //contenerdor de cdi arc
    implementation("io.quarkus:quarkus-arc")
    //rest
    implementation("io.quarkus:quarkus-rest")
    implementation("io.quarkus:quarkus-rest-jsonb")
    implementation("io.quarkus:quarkus-rest-client")
    implementation("io.quarkus:quarkus-rest-client-jsonb")

    //JPA
    implementation("io.quarkus:quarkus-hibernate-orm-panache")
    implementation("io.quarkus:quarkus-jdbc-postgresql")
    //Sirve para mapear fácilmente objetos (ej: de entidad a DTO y viceversa).
    implementation("org.modelmapper:modelmapper:3.2.3")
    //control de versiones
    //Sirve para manejar migraciones de base de datos, usando archivos .sql versionado
    implementation("io.quarkus:quarkus-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")

    //Service Discovery
    implementation("io.quarkus:quarkus-smallrye-stork")
    implementation("io.smallrye.stork:stork-service-discovery-consul")
    //cliente para trabjar con consult de manera reactiva
    implementation("io.smallrye.reactive:smallrye-mutiny-vertx-consul-client")

    //quarkus fault tolerance
    //Resilence, resilencia, tolerancia a fallos
    implementation("io.quarkus:quarkus-smallrye-fault-tolerance")

    //TELEMETRY - metricas
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    implementation("io.quarkus:quarkus-jackson")

    //Health Check
    implementation("io.quarkus:quarkus-smallrye-health")


}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
