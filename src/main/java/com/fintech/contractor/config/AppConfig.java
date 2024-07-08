package com.fintech.contractor.config;

import com.fintech.contractor.auditor.AuditorAware;
import com.fintech.contractor.repository.sql.SQLContractorRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Configuration file for spring boot application
 * @author Matushkin Anton
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.fintech.contractor.repository")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AppConfig {

    @Bean
    public org.springframework.data.domain.AuditorAware<String> auditorProvider() {
        return new AuditorAware();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().load();
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public SQLContractorRepository sqlContractorRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new SQLContractorRepository(namedParameterJdbcTemplate);
    }

}
