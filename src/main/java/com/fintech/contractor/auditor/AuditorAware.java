package com.fintech.contractor.auditor;

import lombok.NonNull;

import java.util.Optional;

public class AuditorAware implements org.springframework.data.domain.AuditorAware<String> {

    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        return Optional.of("username");
    }

}
