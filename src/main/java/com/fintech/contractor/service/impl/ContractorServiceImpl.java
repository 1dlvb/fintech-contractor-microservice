package com.fintech.contractor.service.impl;

import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.repository.ContractorRepository;
import com.fintech.contractor.service.ContractorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractorServiceImpl implements ContractorService {

    @NonNull
    private final ContractorRepository contractorRepository;

    public Contractor saveOrUpdateContractor(Contractor contractor) {
        if (contractor.getId() != null && contractorRepository.existsById(contractor.getId())) {
            Contractor existingContractor = contractorRepository.findById(contractor.getId()).orElse(null);
            if (existingContractor != null) {
                updateProperties(existingContractor, contractor);
                return contractorRepository.save(existingContractor);
            }
        }
        return contractorRepository.save(contractor);
    }

    @Override
    public Contractor findContractorById(String id) {
        Optional<Contractor> contractor = contractorRepository.findById(id);
        return contractor.orElseThrow(() ->
                new EntityNotFoundException("Contractor not found for ID: " + id));
    }

    @Override
    @Transactional
    public void deleteContractor(String id) {
        Contractor contractor = contractorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contractor not found for ID: " + id));
        contractor.setIsActive(false);
        contractorRepository.save(contractor);
    }

    private void updateProperties(Contractor existingContractor, Contractor newContractorData) {
        existingContractor.setParent(newContractorData.getParent());
        existingContractor.setName(newContractorData.getName());
        existingContractor.setNameFull(newContractorData.getNameFull());
        existingContractor.setInn(newContractorData.getInn());
        existingContractor.setOgrn(newContractorData.getOgrn());
        existingContractor.setCountry(newContractorData.getCountry());
        existingContractor.setIndustry(newContractorData.getIndustry());
        existingContractor.setOrgForm(newContractorData.getOrgForm());
        existingContractor.setIsActive(newContractorData.getIsActive());
    }

}
