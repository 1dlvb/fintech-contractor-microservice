package com.fintech.contractor.service.impl;

import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.repository.IndustryRepository;
import com.fintech.contractor.service.IndustryService;
import com.onedlvb.advice.LogLevel;
import com.onedlvb.advice.annotation.AuditLog;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IndustryServiceImpl implements IndustryService {

    @NonNull
    private final IndustryRepository repository;

    @NonNull
    private final ModelMapper modelMapper;

    @Override
    @AuditLog(logLevel = LogLevel.INFO)
    public List<IndustryDTO> fetchAllIndustries() {
        List<Industry> industries = repository.findAll();
        return industries.stream()
                .map(industry -> modelMapper.map(industry, IndustryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @AuditLog(logLevel = LogLevel.INFO)
    public IndustryDTO saveOrUpdateIndustry(IndustryDTO industryDTO) {
        Industry industry = modelMapper.map(industryDTO, Industry.class);
        if (industry.getId() != null && repository.existsById(industry.getId())) {
            Industry existingIndustry = repository.findById(industry.getId()).orElse(null);
            if (existingIndustry != null) {
                updateProperties(existingIndustry, industry);
                industry = repository.save(existingIndustry);
            }
        } else {
            industry = repository.save(industry);
        }
        return modelMapper.map(industry, IndustryDTO.class);
    }

    @Override
    @AuditLog(logLevel = LogLevel.INFO)
    public IndustryDTO findIndustryById(Long id) throws NotActiveException {
        Optional<Industry> industryOptional = repository.findById(id);
        Industry industry = industryOptional.orElseThrow(() ->
                new EntityNotFoundException("Industry not found for ID: " + id));

        if (industry.getIsActive()) {
            return modelMapper.map(industry, IndustryDTO.class);
        } else {
            throw new NotActiveException("Industry is not active");
        }
    }

    @Override
    @AuditLog(logLevel = LogLevel.INFO)
    public void deleteIndustry(Long id) throws NotActiveException {
        Optional<Industry> industryOptional = repository.findById(id);
        Industry industry = industryOptional.orElseThrow(() ->
                new EntityNotFoundException("Industry not found for ID: " + id));
        if (industry.getIsActive()) {
            industry.setIsActive(false);
            repository.save(industry);
        } else {
            throw new NotActiveException("Industry is not active");
        }
    }

    private void updateProperties(Industry existingIndustry, Industry newIndustryData) {
        existingIndustry.setName(newIndustryData.getName());
        existingIndustry.setIsActive(newIndustryData.getIsActive());
    }

}
