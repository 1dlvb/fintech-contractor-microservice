package com.fintech.contractor.service.impl;

import com.fintech.contractor.config.AppConfig;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.service.OrgFormService;
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
public class OrgFormServiceImplTests {


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
    private OrgFormService orgFormService;

    @Test
    public void testFindOrgFormByIdWhenOrgFormNotActiveThrowsNotActiveException() throws NotActiveException {
        Long orgFormId = 1L;
        OrgForm orgForm = new OrgForm();
        orgForm.setId(orgFormId);
        orgForm.setIsActive(false);

        when(orgFormService.findOrgFormById(orgFormId))
                .thenThrow(new NotActiveException("OrgForm is not active"));
        assertThrows(NotActiveException.class, () -> orgFormService.findOrgFormById(orgFormId));
        verify(orgFormService, times(1)).findOrgFormById(orgFormId);
    }


    @Test
    public void testDeleteOrgFormWhenOrgFormNotActiveThrowsNotActiveException() throws NotActiveException {
        Long orgFormId = 1L;
        OrgForm orgForm = new OrgForm();
        orgForm.setId(orgFormId);
        orgForm.setIsActive(false);

        doThrow(new NotActiveException("OrgForm is not active"))
                .when(orgFormService).deleteOrgForm(orgFormId);
        assertThrows(NotActiveException.class, () -> orgFormService.deleteOrgForm(orgFormId));
        verify(orgFormService, times(1)).deleteOrgForm(orgFormId);
    }

}