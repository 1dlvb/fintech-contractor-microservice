package com.fintech.contractor.repository;

import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.model.OrgForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Import(AppConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class ContractorRepositoryTests {


    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    public static void setTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private ContractorRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testRepositorySavesContractor() {
        Contractor newContractor = buildSampleContractor();
        Contractor savedContractor = repository.save(newContractor);
        assertEquals(savedContractor, entityManager.find(Contractor.class, newContractor.getId()),
                "Saved contractor should match the persisted one");
    }

    @Test
    public void testRepositoryUpdatesContractor() {
        Contractor newContractor = buildSampleContractor();
        entityManager.persistAndFlush(newContractor);
        newContractor.setName("Updated Contractor Name");
        repository.save(newContractor);
        assertEquals("Updated Contractor Name",
                entityManager.find(Contractor.class, newContractor.getId()).getName(),
                "Contractor name should be updated");
    }

    @Test
    public void testRepositoryFindsContractorById() {
        Contractor newContractor = buildSampleContractor();
        entityManager.persistAndFlush(newContractor);
        assertTrue(repository.findById(newContractor.getId()).isPresent(),
                "Contractor should be found by ID");
    }

    @Test
    public void testRepositoryDeletesContractorById() {
        Contractor newContractor = buildSampleContractor();
        entityManager.persistAndFlush(newContractor);
        repository.deleteById(newContractor.getId());
        assertNull(entityManager.find(Contractor.class, newContractor.getId()),
                "Contractor should be deleted");
    }

    private Contractor buildSampleContractor() {
        Country country = new Country("CO", "Country", true);
        OrgForm orgForm = new OrgForm(1L, "Org Form", true);
        Industry industry = new Industry(1L, "Industry", true);

        country = entityManager.merge(country);
        orgForm = entityManager.merge(orgForm);
        industry = entityManager.merge(industry);

        entityManager.flush();
        entityManager.clear();

        return Contractor.builder()
                .id("1")
                .name("name")
                .nameFull("name full")
                .inn("123456")
                .ogrn("654321")
                .orgForm(orgForm)
                .country(country)
                .industry(industry)
                .isActive(true)
                .build();
    }
}
