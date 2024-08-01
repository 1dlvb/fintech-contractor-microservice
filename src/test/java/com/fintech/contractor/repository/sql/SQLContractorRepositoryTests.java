package com.fintech.contractor.repository.sql;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.payload.SearchContractorPayload;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class SQLContractorRepositoryTests {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private SQLContractorRepository sqlContractorRepository;

    @DynamicPropertySource
    public static void setTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    @Sql("/sql/contractors_test.sql")
    public void testFindContractorByFilters() {
        SearchContractorPayload filters = new SearchContractorPayload(
                "1",
                null,
                "TechCorp",
                null,
                null,
                null,
                null,
                null,
                "Limited Liability Company"
        );

        List<ContractorDTO> contractors = sqlContractorRepository.findContractorByFilters(filters, 0, 10);
        assertEquals(contractors.get(0).getId(), filters.getId());
        assertEquals(contractors.get(0).getName(), filters.getName());
        assertEquals(contractors.get(0).getOrgForm().getName(), filters.getOrgForm());
    }

}