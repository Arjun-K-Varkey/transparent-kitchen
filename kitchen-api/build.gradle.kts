plugins {
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    java // Ensure the java plugin is active
}

springBoot {
    mainClass.set("com.kitchen.api.KitchenApiApplication")
}

// FORCE JAVA 21 BYTECODE OUTPUT
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<JavaCompile> {
    options.release.set(21) // This is the magic line for ASM compatibility
}

dependencies {
    implementation(project(":module-order"))
    implementation(project(":module-nutrition"))
    implementation(project(":module-common"))

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}