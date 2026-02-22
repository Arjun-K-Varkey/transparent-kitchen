dependencies {
    implementation(project(":module-common"))
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc:3.4.1")
    implementation("io.projectreactor:reactor-core:3.7.0")
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    implementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")// Make sure this version matches the one in build.gradle.kts
}