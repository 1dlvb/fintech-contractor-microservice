package com.fintech.contractor.service.impl;

import com.fintech.contractor.model.Industry;
import com.fintech.contractor.repository.IndustryRepository;
import com.fintech.contractor.service.IndustryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IndustryServiceImpl implements IndustryService {

    @NonNull
    private final IndustryRepository repository;

    @Override
    public List<Industry> fetchAllIndustries() {
        return repository.findAll();
    }

    @Override
    public Industry saveOrUpdateIndustry(Industry industry) {
        if (industry.getId() != null && repository.existsById(industry.getId())) {
            Industry existingIndustry = repository.findById(industry.getId()).orElse(null);
            if (existingIndustry != null) {
                updateProperties(existingIndustry, industry);
                return repository.save(existingIndustry);
            }
        }
        return repository.save(industry);
    }

    @Override
    public Industry findIndustryById(Long id) {
        Optional<Industry> industry = repository.findById(id);
        return industry.orElseThrow(() ->
                new EntityNotFoundException("Industry not found for ID: " + id));
    }

    @Override
    public void deleteIndustry(Long id) {
        Industry industry = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Industry not found for ID: " + id));
        industry.setIsActive(false);
        repository.save(industry);
    }

    private void updateProperties(Industry existingIndustry, Industry newIndustryData) {
        existingIndustry.setName(newIndustryData.getName());
        existingIndustry.setIsActive(newIndustryData.getIsActive());
    }

}
