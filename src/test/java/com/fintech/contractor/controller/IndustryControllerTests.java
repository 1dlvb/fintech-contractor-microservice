package com.fintech.contractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.service.IndustryService;
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
public class IndustryControllerTests {

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
    private IndustryService industryService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void testControllerReturnsIndustryById() throws Exception, NotActiveException {
        Industry sampleIndustry = buildSampleIndustry(1L, "Industry");
        IndustryDTO industryDTO = modelMapper.map(sampleIndustry, IndustryDTO.class);
        when(industryService.findIndustryById(sampleIndustry.getId())).thenReturn(industryDTO);
        mockMvc.perform(get("/industry/{id}", sampleIndustry.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(industryDTO), true));
    }

    @Test
    public void testControllerCreatesNewIndustry() throws Exception {
        Industry sampleIndustry = buildSampleIndustry(1L, "Industry");
        IndustryDTO industryDTO = modelMapper.map(sampleIndustry, IndustryDTO.class);
        when(industryService.saveOrUpdateIndustry(industryDTO)).thenReturn(industryDTO);
        mockMvc.perform(put("/industry/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                IndustryDTO.builder()
                                        .id(sampleIndustry.getId())
                                        .name(sampleIndustry.getName())
                                        .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(industryDTO)));
    }

    @Test
    public void testControllerDeletesIndustryById() throws Exception, NotActiveException {
        Industry sampleIndustry = buildSampleIndustry(1L, "Industry");
        doNothing().when(industryService).deleteIndustry(sampleIndustry.getId());
        mockMvc.perform(delete("/industry/delete/{id}", sampleIndustry.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void testControllerReturnsAllIndustries() throws Exception {
        Industry industry1 = buildSampleIndustry(1L, "Industry 1");
        Industry industry2 = buildSampleIndustry(2L, "Industry 2");
        IndustryDTO industryDTO1 = modelMapper.map(industry1, IndustryDTO.class);
        IndustryDTO industryDTO2 = modelMapper.map(industry2, IndustryDTO.class);
        when(industryService.fetchAllIndustries()).thenReturn(Arrays.asList(industryDTO1, industryDTO2));

        mockMvc.perform(get("/industry/all"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(2),
                        jsonPath("$[0].id").value(industry1.getId()),
                        jsonPath("$[1].id").value(industry2.getId())
                );
    }

    private Industry buildSampleIndustry(Long id, String name) {
        return new Industry(id, name, true);
    }

}
