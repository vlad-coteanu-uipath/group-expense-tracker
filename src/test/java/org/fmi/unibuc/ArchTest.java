package org.fmi.unibuc;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.fmi.unibuc");

        noClasses()
            .that()
                .resideInAnyPackage("org.fmi.unibuc.service..")
            .or()
                .resideInAnyPackage("org.fmi.unibuc.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..org.fmi.unibuc.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
