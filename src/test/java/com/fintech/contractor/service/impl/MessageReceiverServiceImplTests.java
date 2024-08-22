package com.fintech.contractor.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintech.contractor.dto.ContractorWithMainBorrowerDTO;
import com.fintech.contractor.dto.MainBorrowerDTO;
import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.repository.ContractorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
class MessageReceiverServiceImplTests {

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.13.6-management");

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withReuse(true);

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        rabbitMQContainer.start();
        postgreSQLContainer.start();
    }
    @AfterEach
    void tearDown() {
        contractorRepository.deleteAll();
    }
    @Test
    void testReceiveReceivesMessageFromTheQueueAndUpdatesMainBorrower() throws JsonProcessingException {
        ContractorWithMainBorrowerDTO contractorDTO = new ContractorWithMainBorrowerDTO();
        UUID uuid = UUID.randomUUID();
        Contractor contractor = Contractor.builder()
                .id(String.valueOf(uuid))
                .name("contractor")
                .createDate(LocalDateTime.now())
                .createUserId("username")
                .activeMainBorrower(true)
                .isActive(true)
                .build();

        contractorDTO.setId(uuid.toString());
        contractorDTO.setActiveMainBorrower(false);
        contractorRepository.save(contractor);

        amqpTemplate.convertAndSend("fintech-rabbitmq-deal-active-main-borrower-queue",
                objectMapper.writeValueAsString(MainBorrowerDTO.builder()
                        .contractorId(String.valueOf(uuid))
                        .hasMainDeals(true)
                        .build()));
        Contractor contractor1 = contractorRepository.findById(String.valueOf(uuid)).orElse(null);

        assertNotNull(contractor1);
        assertTrue(contractor1.getActiveMainBorrower());

    }
}