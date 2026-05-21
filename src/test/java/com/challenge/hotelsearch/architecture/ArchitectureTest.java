package com.challenge.hotelsearch.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchitectureTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("com.challenge.hotelsearch");

    @Test
    void domainShouldNotDependOnOtherLayers() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "..application..",
                        "..infrastructure.."
                )
                .check(importedClasses);
    }

    @Test
    void domainShouldBeFrameworkIndependent() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "org.hibernate.."
                )
                .check(importedClasses);
    }

    @Test
    void applicationShouldNotDependOnInfrastructure() {
        noClasses()
                .that().resideInAPackage("..application..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "..infrastructure.."
                )
                .check(importedClasses);
    }

    @Test
    void applicationShouldBeFrameworkIndependent() {
        noClasses()
                .that().resideInAPackage("..application..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "org.hibernate.."
                )
                .check(importedClasses);
    }
}
