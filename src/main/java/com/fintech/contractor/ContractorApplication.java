package com.fintech.contractor;

import com.fintech.contractor.auditor.AuditorAware;
import com.fintech.contractor.repository.sql.SQLContractorRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ContractorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractorApplication.class, args);
    }

    @Bean
    public org.springframework.data.domain.AuditorAware<String> auditorProvider() {
        return new AuditorAware();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
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
    public SQLContractorRepository sqlContractorRepository(JdbcTemplate jdbcTemplate) {
        return new SQLContractorRepository(jdbcTemplate);
    }

}
