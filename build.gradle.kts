plugins {
    java
    id("org.springframework.boot") version "3.4.1" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    // This ensures subprojects inherit the version BOM from Spring Boot
    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.1")
        }
    }

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.release.set(21)
    }

    dependencies {
        // Now that the BOM is imported above, we don't need to specify the version here
        implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
        
        // Use the official PostgreSQL R2DBC driver
        implementation("org.postgresql:r2dbc-postgresql:1.0.7.RELEASE")
    }
}