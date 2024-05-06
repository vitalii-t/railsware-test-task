plugins {
    id("java")
    id("maven-publish")
}

group = "io.mailtrap.testtask"
version = "1.0"

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.mailtrap.testtask"
            artifactId = "java-sdk-test-task"
            version = "1.0"

            from(components["java"])
        }
    }
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("jakarta.validation:jakarta.validation-api:3.1.0-M2")
    implementation("org.jetbrains:annotations:24.1.0")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
    implementation("javax.el:javax.el-api:3.0.1-b06")
    implementation("org.glassfish.expressly:expressly:5.0.0")
    compileOnly("org.projectlombok:lombok:1.18.32")
    compileOnly("org.projectlombok:lombok:1.18.32")

    annotationProcessor("org.projectlombok:lombok:1.18.32")

    testImplementation("org.wiremock:wiremock:3.5.4")
    testImplementation(platform("org.junit:junit-bom:5.11.0-M1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testCompileOnly("org.projectlombok:lombok:1.18.32")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.32")
}

tasks.test {
    useJUnitPlatform()
}