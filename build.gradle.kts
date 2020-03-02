import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
	id("org.springframework.boot") version "2.2.5.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"
	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
}

group = "com.romankagan"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}

	testCompile("org.springframework.boot:spring-boot-starter-test")
	testCompile("org.apache.httpcomponents:httpclient:4.5.11")
}

val fatJar = task("fatJar", type = Jar::class) {
	baseName = "pyramid-fat"
	// manifest Main-Class attribute is optional.
	// (Used only to provide default main class for executable jar)
	manifest {
		// fully qualified class name of default main class
		attributes["Main-Class"] = "com.romankagan.pyramid.Application"
	}
//	from(configurations.runtime.map({ if (it.isDirectory) it else zipTree(it) }))
	from(configurations.runtime.get().map({ if (it.isDirectory) it else zipTree(it) }))
	with(tasks["jar"] as CopySpec)
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks {
	"build" {
		dependsOn(fatJar)
	}
}