package com.fintech.contractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.service.CountryService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(AppConfig.class)
@SpringBootTest
@Testcontainers
public class CountryControllerTests {

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
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private CountryService countryService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void testControllerReturnsCountryById() throws Exception, NotActiveException {
        Country sampleCountry = buildSampleCountry("Co", "Country");
        CountryDTO countryDTO = modelMapper.map(sampleCountry, CountryDTO.class);
        when(countryService.findCountryById(sampleCountry.getId())).thenReturn(countryDTO);
        mockMvc.perform(get("/country/{id}", sampleCountry.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(countryDTO), true));
    }

    @Test
    public void testControllerCreatesNewCountry() throws Exception {
        Country sampleCountry = buildSampleCountry("Co", "Country");
        CountryDTO countryDTO = modelMapper.map(sampleCountry, CountryDTO.class);
        when(countryService.saveOrUpdateCountry(countryDTO)).thenReturn(countryDTO);
        mockMvc.perform(put("/country/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                CountryDTO.builder()
                                        .id(sampleCountry.getId())
                                        .name(sampleCountry.getName())
                                        .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(countryDTO)));
    }

    @Test
    public void testControllerDeletesCountryById() throws Exception, NotActiveException {
        Country sampleCountry = buildSampleCountry("CO", "Country");
        doNothing().when(countryService).deleteCountry(sampleCountry.getId());
        mockMvc.perform(delete("/country/delete/{id}", sampleCountry.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testControllerReturnsAllCountries() throws Exception {
        Country country1 = buildSampleCountry("CO1", "Country 1");
        Country country2 = buildSampleCountry("CO2", "Country 2");
        CountryDTO countryDTO1 = modelMapper.map(country1, CountryDTO.class);
        CountryDTO countryDTO2 = modelMapper.map(country2, CountryDTO.class);
        when(countryService.fetchAllCountries()).thenReturn(Arrays.asList(countryDTO1, countryDTO2));

        mockMvc.perform(get("/country/all"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(2),
                        jsonPath("$[0].id").value(country1.getId()),
                        jsonPath("$[1].id").value(country2.getId())
                );
    }


    private Country buildSampleCountry(String id, String name) {
        return new Country(id, name, true);
    }

}
