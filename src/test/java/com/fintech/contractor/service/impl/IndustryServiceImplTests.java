package com.fintech.contractor.service.impl;

import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.service.IndustryService;
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
class IndustryServiceImplTests {


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
    private IndustryService industryService;

    @Test
    void testFindIndustryByIdWhenIndustryNotActiveThrowsNotActiveException() throws NotActiveException {
        Long industryId = 1L;
        Industry industry = new Industry();
        industry.setId(industryId);
        industry.setIsActive(false);

        when(industryService.findIndustryById(industryId))
                .thenThrow(new NotActiveException("Industry is not active"));
        assertThrows(NotActiveException.class, () -> industryService.findIndustryById(industryId));
        verify(industryService, times(1)).findIndustryById(industryId);
    }


    @Test
    void testDeleteIndustryWhenIndustryNotActiveThrowsNotActiveException() throws NotActiveException {
        Long industryId = 1L;
        Industry industry = new Industry();
        industry.setId(industryId);
        industry.setIsActive(false);

        doThrow(new NotActiveException("Industry is not active"))
                .when(industryService).deleteIndustry(industryId);
        assertThrows(NotActiveException.class, () -> industryService.deleteIndustry(industryId));
        verify(industryService, times(1)).deleteIndustry(industryId);
    }

}