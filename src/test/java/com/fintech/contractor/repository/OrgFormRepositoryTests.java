package com.fintech.contractor.repository;

import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.model.OrgForm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(AppConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class OrgFormRepositoryTests {

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
    private OrgFormRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @AfterEach
    public void tearDown() {
        entityManager.getEntityManager().createQuery("DELETE FROM OrgForm").executeUpdate();
        entityManager.clear();
    }

    @Test
    @Transactional
    @DirtiesContext
    void testRepositorySavesOrgForm() {
        OrgForm newOrgForm = buildSampleOrgForm("OrgForm 1");
        OrgForm savedOrgForm = repository.save(newOrgForm);
        assertEquals(savedOrgForm, entityManager.find(OrgForm.class, savedOrgForm.getId()),
                "Saved orgForm should match the persisted one");
    }

    @Test
    @Transactional
    @DirtiesContext
    void testRepositoryUpdatesOrgForm() {
        OrgForm newOrgForm = buildSampleOrgForm("OrgForm");
        newOrgForm = entityManager.merge(newOrgForm);
        newOrgForm.setName("Updated OrgForm Name");
        repository.save(newOrgForm);
        assertEquals("Updated OrgForm Name",
                entityManager.find(OrgForm.class, newOrgForm.getId()).getName(),
                "OrgForm name should be updated");
    }

    @Test
    @Transactional
    @DirtiesContext
    void testRepositoryFindsOrgFormById() {
        OrgForm newOrgForm = buildSampleOrgForm("OrgForm 2");
        newOrgForm = entityManager.merge(newOrgForm);
        assertTrue(repository.findById(newOrgForm.getId()).isPresent(),
                "OrgForm should be found by ID");
    }

    @Test
    @Transactional
    @DirtiesContext
    void testRepositoryDeletesOrgFormById() {
        OrgForm newOrgForm = buildSampleOrgForm("OrgForm");
        newOrgForm = entityManager.merge(newOrgForm);
        repository.deleteById(newOrgForm.getId());
        assertNull(entityManager.find(OrgForm.class, newOrgForm.getId()),
                "OrgForm should be deleted");
    }

    @Test
    @Transactional
    @DirtiesContext
    void testRepositoryReturnsAllIndustries() {
        OrgForm newOrgForm1 = buildSampleOrgForm("OrgForm 1");
        OrgForm newOrgForm2 = buildSampleOrgForm("OrgForm 2");
        newOrgForm1 = entityManager.merge(newOrgForm1);
        newOrgForm2 = entityManager.merge(newOrgForm2);
        entityManager.flush();
        List<OrgForm> orgForms = repository.findAll();
        assertTrue(orgForms.containsAll(Arrays.asList(newOrgForm1, newOrgForm2)),
                "Should return all persisted orgForms");
    }

    private OrgForm buildSampleOrgForm(String name) {
        return new OrgForm(null, name + "-" + UUID.randomUUID(), true);
    }
}
