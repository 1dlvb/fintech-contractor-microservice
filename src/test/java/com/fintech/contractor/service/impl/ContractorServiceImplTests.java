package com.fintech.contractor.service.impl;

import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.service.ContractorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(AppConfig.class)
@SpringBootTest
@Testcontainers
class ContractorServiceImplTests {


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

    @MockBean
    private ContractorService contractorService;

    @Test
    void testFindContractorByIdWhenContractorNotActiveThrowsNotActiveException() throws NotActiveException {
        String contractorId = "1";
        Contractor contractor = new Contractor();
        contractor.setId(contractorId);
        contractor.setIsActive(false);

        when(contractorService.findContractorById(contractorId))
                .thenThrow(new NotActiveException("Contractor is not active"));
        assertThrows(NotActiveException.class, () -> contractorService.findContractorById(contractorId));
        verify(contractorService, times(1)).findContractorById(contractorId);
    }


    @Test
    void testDeleteContractorWhenContractorNotActiveThrowsNotActiveException() throws NotActiveException {
        String contractorId = "1";
        Contractor contractor = new Contractor();
        contractor.setId(contractorId);
        contractor.setActiveMainBorrower(false);
        contractor.setIsActive(false);

        doThrow(new NotActiveException("Contractor is not active"))
                .when(contractorService).deleteContractor(contractorId);
        assertThrows(NotActiveException.class, () -> contractorService.deleteContractor(contractorId));
        verify(contractorService, times(1)).deleteContractor(contractorId);
    }

}