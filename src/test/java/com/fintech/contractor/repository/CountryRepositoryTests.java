package com.fintech.contractor.repository;

import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.model.Country;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Import(AppConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CountryRepositoryTests {

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
    private CountryRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testRepositorySavesCountry() {
        Country newCountry = buildSampleCountry("CO", "Country");
        Country savedCountry = repository.save(newCountry);
        assertEquals(savedCountry, entityManager.find(Country.class, newCountry.getId()),
                "Saved country should match the persisted one");
    }

    @Test
    public void testRepositoryUpdatesCountry() {
        Country newCountry = buildSampleCountry("CO", "Country");
        entityManager.persistAndFlush(newCountry);
        newCountry.setName("Updated Country Name");
        repository.save(newCountry);
        assertEquals("Updated Country Name",
                entityManager.find(Country.class, newCountry.getId()).getName(),
                "Country name should be updated");
    }

    @Test
    public void testRepositoryFindsCountryById() {
        Country newCountry = buildSampleCountry("CO", "Country");
        entityManager.persistAndFlush(newCountry);
        assertTrue(repository.findById(newCountry.getId()).isPresent(),
                "Country should be found by ID");
    }

    @Test
    public void testRepositoryDeletesCountryById() {
        Country newCountry = buildSampleCountry("CO", "Country");
        entityManager.persistAndFlush(newCountry);
        repository.deleteById(newCountry.getId());
        assertNull(entityManager.find(Country.class, newCountry.getId()),
                "Country should be deleted");
    }

    @Test
    public void testRepositoryReturnsAllCountries() {
        entityManager.clear();
        Country newCountry1 = buildSampleCountry("UK", "United Kingdom");
        Country newCountry2 = buildSampleCountry("US", "United States");
        entityManager.persist(newCountry1);
        entityManager.persist(newCountry2);
        entityManager.flush();
        List<Country> countries = repository.findAll();
        assertTrue(countries.containsAll(Arrays.asList(newCountry1, newCountry2)),
                "Should return all persisted countries");
    }

    private Country buildSampleCountry(String id, String name) {
        return new Country(id, name, true);
    }
}
