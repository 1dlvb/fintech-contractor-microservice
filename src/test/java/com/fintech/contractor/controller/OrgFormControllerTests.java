package com.fintech.contractor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.service.OrgFormService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
class OrgFormControllerTests {

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
    private OrgFormService orgFormService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setupSecurityContext() {
        UserDetails user = User.withUsername("testUser")
                .password("password")
                .authorities(new SimpleGrantedAuthority("SUPERUSER"))
                .build();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        SecurityContextHolder.setContext(context);
    }
    @Test
    void testControllerReturnsOrgFormById() throws Exception {
        OrgForm sampleOrgForm = buildSampleOrgForm(1L, "OrgForm");
        OrgFormDTO orgFormDTO = modelMapper.map(sampleOrgForm, OrgFormDTO.class);
        when(orgFormService.findOrgFormById(sampleOrgForm.getId())).thenReturn(orgFormDTO);
        mockMvc.perform(get("/contractor/org_form/{id}", sampleOrgForm.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orgFormDTO), true));
    }

    @Test
    void testControllerCreatesNewOrgForm() throws Exception {
        OrgForm sampleOrgForm = buildSampleOrgForm(1L, "OrgForm");
        OrgFormDTO orgFormDTO = modelMapper.map(sampleOrgForm, OrgFormDTO.class);
        when(orgFormService.saveOrUpdateOrgForm(orgFormDTO)).thenReturn(orgFormDTO);
        mockMvc.perform(put("/contractor/org_form/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                OrgFormDTO.builder()
                                        .id(sampleOrgForm.getId())
                                        .name(sampleOrgForm.getName())
                                        .build()
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orgFormDTO)));
    }

    @Test
    void testControllerDeletesOrgFormById() throws Exception {
        OrgForm sampleOrgForm = buildSampleOrgForm(1L, "OrgForm");
        doNothing().when(orgFormService).deleteOrgForm(sampleOrgForm.getId());
        mockMvc.perform(delete("/contractor/org_form/delete/{id}", sampleOrgForm.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void testControllerReturnsAllOrgForms() throws Exception {
        OrgForm orgForm1 = buildSampleOrgForm(1L, "OrgForm 1");
        OrgForm orgForm2 = buildSampleOrgForm(2L, "OrgForm 2");
        OrgFormDTO orgFormDTO1 = modelMapper.map(orgForm1, OrgFormDTO.class);
        OrgFormDTO orgFormDTO2 = modelMapper.map(orgForm2, OrgFormDTO.class);
        when(orgFormService.fetchAllOrgForms()).thenReturn(Arrays.asList(orgFormDTO1, orgFormDTO2));

        mockMvc.perform(get("/contractor/org_form/all"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(2),
                        jsonPath("$[0].id").value(orgForm1.getId()),
                        jsonPath("$[1].id").value(orgForm2.getId())
                );
    }
    private OrgForm buildSampleOrgForm(Long id, String name) {
        return new OrgForm(id, name, true);
    }

}
