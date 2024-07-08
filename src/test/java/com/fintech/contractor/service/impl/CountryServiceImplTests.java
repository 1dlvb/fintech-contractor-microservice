package com.fintech.contractor.service.impl;

import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.service.CountryService;
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
public class CountryServiceImplTests {


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
    private CountryService countryService;

    @Test
    public void testFindCountryByIdWhenCountryNotActiveThrowsNotActiveException() throws NotActiveException {
        String countryId = "1";
        Country country = new Country();
        country.setId(countryId);
        country.setIsActive(false);

        when(countryService.findCountryById(countryId))
                .thenThrow(new NotActiveException("Country is not active"));
        assertThrows(NotActiveException.class, () -> countryService.findCountryById(countryId));
        verify(countryService, times(1)).findCountryById(countryId);
    }


    @Test
    public void testDeleteCountryWhenCountryNotActiveThrowsNotActiveException() throws NotActiveException {
        String countryId = "1";
        Country country = new Country();
        country.setId(countryId);
        country.setIsActive(false);

        doThrow(new NotActiveException("Country is not active"))
                .when(countryService).deleteCountry(countryId);
        assertThrows(NotActiveException.class, () -> countryService.deleteCountry(countryId));
        verify(countryService, times(1)).deleteCountry(countryId);
    }

}