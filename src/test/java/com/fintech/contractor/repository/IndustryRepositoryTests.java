package com.fintech.contractor.repository;

import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.model.Industry;
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
public class IndustryRepositoryTests {

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
    private IndustryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @AfterEach
    public void tearDown() {
        entityManager.getEntityManager().createQuery("DELETE FROM Industry").executeUpdate();
        entityManager.clear();
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testRepositorySavesIndustry() {
        Industry newIndustry = buildSampleIndustry("Industry 1");
        Industry savedIndustry = repository.save(newIndustry);
        assertEquals(savedIndustry, entityManager.find(Industry.class, savedIndustry.getId()),
                "Saved industry should match the persisted one");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testRepositoryUpdatesIndustry() {
        Industry newIndustry = buildSampleIndustry("Industry");
        newIndustry = entityManager.merge(newIndustry);
        newIndustry.setName("Updated Industry Name");
        repository.save(newIndustry);
        assertEquals("Updated Industry Name",
                entityManager.find(Industry.class, newIndustry.getId()).getName(),
                "Industry name should be updated");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testRepositoryFindsIndustryById() {
        Industry newIndustry = buildSampleIndustry("Industry 2");
        newIndustry = entityManager.merge(newIndustry);
        assertTrue(repository.findById(newIndustry.getId()).isPresent(),
                "Industry should be found by ID");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testRepositoryDeletesIndustryById() {
        Industry newIndustry = buildSampleIndustry("Industry");
        newIndustry = entityManager.merge(newIndustry);
        repository.deleteById(newIndustry.getId());
        assertNull(entityManager.find(Industry.class, newIndustry.getId()),
                "Industry should be deleted");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testRepositoryReturnsAllIndustries() {
        Industry newIndustry1 = buildSampleIndustry("Industry 1");
        Industry newIndustry2 = buildSampleIndustry("Industry 2");
        newIndustry1 = entityManager.merge(newIndustry1);
        newIndustry2 = entityManager.merge(newIndustry2);
        entityManager.flush();
        List<Industry> industries = repository.findAll();
        assertTrue(industries.containsAll(Arrays.asList(newIndustry1, newIndustry2)),
                "Should return all persisted industries");
    }

    private Industry buildSampleIndustry(String name) {
        return new Industry(null, name + "-" + UUID.randomUUID(), true);
    }
}
