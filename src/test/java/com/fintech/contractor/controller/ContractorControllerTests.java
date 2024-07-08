package com.fintech.contractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.service.ContractorService;
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
public class ContractorControllerTests {
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
    private ContractorService contractorService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void testControllerReturnsContractorById() throws Exception, NotActiveException {
        Contractor sampleContractor = buildSampleContractor();
        ContractorDTO contractorDTO = modelMapper.map(sampleContractor, ContractorDTO.class);
        when(contractorService.findContractorById(sampleContractor.getId())).thenReturn(contractorDTO);
        mockMvc.perform(get("/contractor/{id}", sampleContractor.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contractorDTO), true));
    }

    @Test
    public void testControllerCreatesNewContractor() throws Exception {
        Contractor sampleContractor = buildSampleContractor();
        ContractorDTO contractorDTO = modelMapper.map(sampleContractor, ContractorDTO.class);
        when(contractorService.saveOrUpdateContractor(contractorDTO)).thenReturn(contractorDTO);
        mockMvc.perform(put("/contractor/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                ContractorDTO.builder()
                                        .id(sampleContractor.getId())
                                        .parent(sampleContractor.getParent())
                                        .name(sampleContractor.getName())
                                        .nameFull(sampleContractor.getNameFull())
                                        .inn(sampleContractor.getInn())
                                        .ogrn(sampleContractor.getOgrn())
                                        .country(CountryDTO.builder()
                                                .id(sampleContractor.getCountry().getId())
                                                .name(sampleContractor.getCountry().getName())
                                                .build())
                                        .industry(IndustryDTO.builder()
                                                .id(sampleContractor.getIndustry().getId())
                                                .name(sampleContractor.getIndustry().getName())
                                                .build())
                                        .orgForm(OrgFormDTO.builder()
                                                .id(sampleContractor.getOrgForm().getId())
                                                .name(sampleContractor.getOrgForm().getName())
                                                .build())
                                        .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(contractorDTO)));
    }

    @Test
    public void testControllerDeletesContractorById() throws Exception, NotActiveException {
        Contractor sampleContractor = buildSampleContractor();
        doNothing().when(contractorService).deleteContractor(sampleContractor.getId());
        mockMvc.perform(delete("/contractor/delete/{id}", sampleContractor.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    private Contractor buildSampleContractor() {
        return Contractor.builder()
                .id("1")
                .name("Contractor 1")
                .nameFull("Contractor 1 full")
                .inn("123456")
                .ogrn("12345678")
                .orgForm(new OrgForm(1L, "Sample OrgForm", true))
                .country(new Country("UK", "United Kingdom", true))
                .industry(new Industry(1L, "Sample Industry", true))
                .isActive(true)
                .build();
    }

}
