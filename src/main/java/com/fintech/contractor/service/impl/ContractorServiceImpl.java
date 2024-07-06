package com.fintech.contractor.service.impl;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.payload.SearchContractorPayload;
import com.fintech.contractor.repository.ContractorRepository;
import com.fintech.contractor.repository.specification.ContractorSpecification;
import com.fintech.contractor.repository.sql.SQLContractorRepository;
import com.fintech.contractor.service.ContractorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractorServiceImpl implements ContractorService {

    @NonNull
    private final ContractorRepository contractorRepository;

    @NonNull
    private final SQLContractorRepository sqlContractorRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @Override
    public List<ContractorDTO> findContractors(SearchContractorPayload payload, Pageable pageable) {
        Specification<Contractor> spec = ContractorSpecification.findContractorsBySpecifications(payload);
        Page<Contractor> contractors = contractorRepository.findAll(spec, pageable);
        return contractors.stream().map(contractor -> modelMapper.map(contractor, ContractorDTO.class)).toList();
    }

    @Override
    public List<ContractorDTO> findContractorsSQL(SearchContractorPayload payload, Integer page, Integer size) {
        return sqlContractorRepository.findContractorByFilters(payload, page, size);
    }

    @Override
    public ContractorDTO saveOrUpdateContractor(ContractorDTO contractorDTO) {
        Contractor contractor = modelMapper.map(contractorDTO, Contractor.class);
        if (contractor.getId() != null && contractorRepository.existsById(contractor.getId())) {
            Contractor existingContractor = contractorRepository.findById(contractor.getId()).orElse(null);
            if (existingContractor != null) {
                updateProperties(existingContractor, contractor);
                contractor = contractorRepository.save(existingContractor);
            }
        } else {
            contractor = contractorRepository.save(contractor);
        }
        return modelMapper.map(contractor, ContractorDTO.class);
    }

    @Override
    public ContractorDTO findContractorById(String id) throws NotActiveException {
        Optional<Contractor> contractorOptional = contractorRepository.findById(id);
        Contractor contractor = contractorOptional.orElseThrow(() ->
                new EntityNotFoundException("Contractor not found for ID: " + id));

        if (contractor.getIsActive()) {
            return modelMapper.map(contractor, ContractorDTO.class);
        } else {
            throw new NotActiveException("Contractor is not active");
        }
    }

    @Override
    @Transactional
    public void deleteContractor(String id) throws NotActiveException {
        Optional<Contractor> contractorOptional = contractorRepository.findById(id);
        Contractor contractor = contractorOptional.orElseThrow(() ->
                new EntityNotFoundException("Contractor not found for ID: " + id));
        if (contractor.getIsActive()) {
            contractor.setIsActive(false);
            contractorRepository.save(contractor);
        } else {
            throw new NotActiveException("Contractor is not active");
        }
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
